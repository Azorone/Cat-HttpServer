package org.cathttp.base.net;

import org.cathttp.base.net.inter.LifeCycle;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class BusinessThreadPool implements LifeCycle {
    private final AtomicInteger MAX_TEMP_SIZE = new AtomicInteger(0);
    private PoolConfig config;
    private static final BusinessThreadPool businessThreadPool = new BusinessThreadPool();
    private BusinessThreadPool(){
       this.config = new PoolConfig();
    }

   public static BusinessThreadPool getBusinessThreadPool(){
       return businessThreadPool;
   }
    private static ThreadPoolExecutor poolExecutor ;
    public class PoolConfig{
        int coreSize = 10;
        int max = 16;
        long alive = 10;
        TimeUnit unit = TimeUnit.MINUTES;
        BlockingQueue blockingQueue = new ArrayBlockingQueue<>(100) ;
        RejectedExecutionHandler rej = new ThreadPoolExecutor.AbortPolicy();

        public PoolConfig(int coreSize, int max, long alive, TimeUnit unit, BlockingQueue<?> blockingQueue, RejectedExecutionHandler rej) {
            this.coreSize = coreSize;
            this.max = max;
            this.alive = alive;
            this.unit = unit;
            this.blockingQueue = blockingQueue;
            this.rej = rej;
        }

        public PoolConfig(){};

    }
    public void config(int coreSize,
                       int max,
                       long alive,
                       TimeUnit unit,
                       BlockingQueue blockingQueue,
                       RejectedExecutionHandler rej){
       this.config = new PoolConfig(coreSize,
        max, alive, unit, blockingQueue, rej);
    }
    public PoolConfig configPoolCoreSize(int coreSize){
        config.coreSize = coreSize;
        return this.config;
    }

    public PoolConfig configPoolMaxSize(int max){
        config.max =max;
        return this.config;
    }

    public PoolConfig configPoolAlive(long time){
        config.alive = time;
        return this.config;
    }

    public PoolConfig configPoolTimeUnit(TimeUnit unit){
        config.unit = unit;
        return this.config;
    }

    public PoolConfig configPoolBlock(BlockingQueue<?> blockingQueue){
        config.blockingQueue= blockingQueue;
        return this.config;
    }
    public PoolConfig configPoolRej(RejectedExecutionHandler rej){
        config.rej= rej;
        return this.config;
    }
    @Override
    public void init() {
        System.out.println("线程池初始化中...");
        if (this.config == null){
            this.config = new PoolConfig();
        }
        poolExecutor = new ThreadPoolExecutor(
                config.coreSize
                ,config.max,
                config.alive,
                config.unit,
                config.blockingQueue,
                new BusinessThreadThreadFactory(),
                config.rej);
    }

    @Override
    public void pause() {
        System.out.println("线程池销毁中...");
            poolExecutor.shutdown();
    }
    long sleepMoRen = 100;
    public void submit(Runnable runnable) throws InterruptedException {
        int key = 0;
        try {
            poolExecutor.execute(runnable);
        }catch (RejectedExecutionException r){
            System.out.println("被拒绝");
            Thread.sleep(100);
            key = 1;
            while (key >0){
                try {
                    poolExecutor.execute(runnable);
                    System.out.println("被拒绝次数："+key);
                    key = 0;
                }catch (RejectedExecutionException r0){
                    Thread.sleep(sleepMoRen);
                    key = key + 1;
                }
            }
        }
    }
}
