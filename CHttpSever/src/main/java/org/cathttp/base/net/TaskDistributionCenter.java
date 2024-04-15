package org.cathttp.base.net;

import org.cathttp.base.net.inter.DistributionCenter;
import org.cathttp.base.net.inter.LifeCycle;

import java.nio.channels.SocketChannel;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
/**
 * ACCEPT 控制中分发中心是否接受新任务 false 不接受
 *
 *
 * **/
public class TaskDistributionCenter implements DistributionCenter<SocketChannel>,Runnable, LifeCycle {

    BlockingQueue<SocketChannel> SocketChannelQueue;
    BlockingQueue<HttpTask> tasksQueue;
    AtomicLong MAX;
    private final AtomicBoolean ACCEPT = new AtomicBoolean(true);
    int num =0;
    private boolean endOver = false;

    int w_size = 0;
    Worker[] workers;

    public TaskDistributionCenter(Worker[] worker, BlockingQueue<SocketChannel> tasksQueue){
        this.workers = worker;
        this.SocketChannelQueue = tasksQueue;
        w_size = worker.length;
    }
    TaskDistributionCenter(Worker []worker,BlockingQueue<HttpTask> tasksQueue ,long max){
        this.workers = worker;
        this.tasksQueue = tasksQueue;
        w_size = worker.length;
        MAX = new AtomicLong(max);
    }
    @Override
    public boolean input(SocketChannel cl) {

        if (ACCEPT.get()){
            SocketChannelQueue.add(cl);
            return true;
        }
        return false;
    }

    @Override
    public boolean distribution() throws InterruptedException {
          SocketChannel cl = null;
          boolean  get = false;
           try {
               cl =  take();
               get = true;
           }catch (InterruptedException e){
               //停止处理？
           }
           if (get){
             HttpTask task = new HttpTask(cl, TaskStatus.REGISTER_WAIT);
             try {
                 workers[getWorkerIndex()].submit(task);
             }catch
             (IllegalStateException exception){
             }
           }
        return get;
    }

    public int getWorkerIndex(){ //用来计算把任务分给哪一个处理线程
       int v = num % w_size;
       if (num > Integer.MAX_VALUE-60){num=-1;}
         num++;
       return v   ;
    }
    public SocketChannel take() throws InterruptedException {
        return  SocketChannelQueue.take();
    }

    @Override
    public void run() {
        boolean run = true;
        System.out.println("调度者初始化完成");
        while (run){
            try {
                distribution();
            } catch (InterruptedException e) {
               if (SocketChannelQueue.isEmpty()){
                   run = false;
               }
            }
        }
    }
    public void acceptEnd(){
            this.ACCEPT.compareAndSet(true,false);
    }
    public void acceptOk(){
        this.ACCEPT.compareAndSet(false,true);
    }
    @Override
    public void init() {
        System.out.println("调度者初始化中...");
        if (workers!=null){
            for(int i=0;i< workers.length;i++){
                workers[i] = new Worker(i,new LinkedBlockingQueue<>());
                workers[i].init();
                workers[i].start();
            }
        }

    }
    @Override
    public void pause() {
        acceptEnd();
        if (endOver){
            for (int i=0;i< workers.length;i++){
                workers[i].pause();
            }
        }
    }
}
