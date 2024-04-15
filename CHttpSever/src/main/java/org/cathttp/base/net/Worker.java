package org.cathttp.base.net;

import org.cathttp.base.app.ServletAppWeb;
import org.cathttp.base.buffer.BuddyMalloc;
import org.cathttp.base.buffer.Malloc;
import org.cathttp.base.buffer.Message;
import org.cathttp.base.net.inter.LifeCycle;
import org.cathttp.http.respone.HttpResBuilder;
import org.cathttp.http.RequestBuilder;
import org.cathttp.javaee.request.ServletRequestImplement;
import org.cathttp.http.ResponseBuilder;
import org.cathttp.javaee.response.ServletOutStreamImp;
import org.cathttp.javaee.response.ServletResponseImplements;

import javax.servlet.ServletException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.BlockingQueue;

public class Worker extends Thread implements LifeCycle {

   private Selector selector;
   //ServletContextImp contextImp;
   private final BusinessThreadPool businessThreadPool = BusinessThreadPool.getBusinessThreadPool();
   //private Selector selectorNew; //用于空轮询重建
   BlockingQueue<HttpTask> blockingQueue;
  private static final ServletAppWeb servletAppWeb =ServletAppWeb.getInstance();

  private  final Malloc malloc = new BuddyMalloc(256,65536,true);
  public   Worker(int i,BlockingQueue<HttpTask> blockingQueue){
        this.setName("WORKER-"+"["+i+"]");
        this.blockingQueue = blockingQueue;
    }
    //当前任务队列满了，抛出异常，分发者捕获异常进行处理
    public void submit(HttpTask task) throws IllegalStateException{
            if (task == null){return;}
            blockingQueue.add(task);
    }
    @Override
    public void run() {
      System.out.println(Thread.currentThread().getName()+" 初始化完成");
                while (true){
                       this.selected();
                        register();
                        runKey();
                }
    }
    public boolean runKey(){
        Set<SelectionKey> selectionKeys = this.selector.selectedKeys();
        Iterator<SelectionKey> iterator = selectionKeys.iterator();
        while (iterator.hasNext()){
            SelectionKey key = iterator.next();
            if (key.isReadable()){
                readKey(key);
            }
            if (key.isWritable()){
                writeKey(key);
            }
        iterator.remove();
        }
        return true;
    }


    public void readKey(SelectionKey key)  {
      ByteBuffer buffer = ByteBuffer.allocate(1024*2);
        byte[] temp;
        int size = 0;
        if (key.isValid()){
            SocketChannel socketChannel = (SocketChannel) key.channel();
            if (socketChannel.isOpen()&&socketChannel.isConnected()){
                try {
                    socketChannel.read(buffer);
                    size = buffer.position();
                    temp = new byte[size];
                    buffer.flip();
                    buffer.get(temp);

                   final ServletRequestImplement finalRequestImplement = RequestBuilder.builder(temp);
                   final ServletResponseImplements finalServletResponseImplements = ResponseBuilder.builderRes();
                    businessThreadPool.submit(() -> {
                        try {
                            if (finalRequestImplement!=null){
                                servletAppWeb.run(finalRequestImplement,finalServletResponseImplements );
                            }
                            key.attach(finalServletResponseImplements);
                            key.interestOps(SelectionKey.OP_WRITE);
                        } catch (ServletException e) {
                            throw new RuntimeException(e);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    });

                }catch (IOException e){
                    System.out.println("读异常");
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }else {
                try {
                    socketChannel.close();
                }catch (IOException ioException){
                    System.out.println("读时对象被断开");
                }
            }
        }
    }

    private void writeK(SelectionKey key){
        //Message message = malloc.allocate(httpResponse.length());
        //message.fill(httpResponse);
        ServletResponseImplements responseImplements =(ServletResponseImplements)  key.attachment();
        byte[] bs= null;
        Message message = null;
        if (responseImplements.getMessage()==null){
            byte[] bh = HttpResBuilder.buildHttpRes(responseImplements.getResult()) ;
            byte[] bb = new byte[0];
            try {
                bb= ((ServletOutStreamImp) responseImplements.getOutputStream()).getByte();
                bs = new byte[bh.length + bb.length];
                System.arraycopy(bh,0,bs,0,bh.length);
                System.arraycopy(bb,0,bs,bh.length,bb.length) ;
                message = malloc.allocate(bs.length);
                message.fill(bs);
            }catch (Exception E){

            }

        }else {
            message = responseImplements.getMessage();
        }

     /*   int l = bs.length%1024;
        int all = bs.length;
        if (l !=0){
            l = bs.length/1024+ 1;
        }else {
            l= bs.length/ 1024;
        }*/
        SocketChannel socketChannel = (SocketChannel) key.channel();
     /*   ByteBuffer[] buffers = new ByteBuffer[100];
        int start = 0;
        for (int i =0;i<l;i++){

            buffers[i] = ByteBuffer.allocateDirect(1024);
            if (all>=1024){
                buffers[i].put(bs,start,1024);
                all= all-1024;
                start += 1024;
                buffers[i].flip();
            }else {
                buffers[i].put(bs,start,all);
                buffers[i].flip();
            }
        }*/
        if (socketChannel.isConnected()&&socketChannel.isOpen()){

            //     ByteBuffer buffer = ByteBuffer.allocate(1024*4);
            try {
                //   buffer.put(httpResponse.getBytes());
                // buffer.flip();
                int over = 0;
                //    while ()
                if (message.messageToSocket(socketChannel,false)){

                    key.interestOps(0);
                    key.cancel();
                    message.free();
                    message = null;
                    socketChannel.close();
                }else {
                    responseImplements.setMessage(message);

                    key.interestOps(0);
                    SelectionKey newKey =  socketChannel.register(this.selector,SelectionKey.OP_WRITE);
                    newKey.attach(responseImplements);
                }

            }catch (Exception e){
                e.printStackTrace();
            }
        }

    }

    private void writeKeyByteArry(SelectionKey key){
        ServletResponseImplements responseImplements =(ServletResponseImplements)  key.attachment();
        byte[] bs= null;
        byte[] bh = HttpResBuilder.buildHttpRes(responseImplements.getResult()) ;
        byte[] bb = new byte[0];

        if (responseImplements.getWrite()==null){
            try {
                bb= ((ServletOutStreamImp) responseImplements.getOutputStream()).getByte();
            }catch (Exception E){

            }

            bs = new byte[bh.length + bb.length];
            System.arraycopy(bh,0,bs,0,bh.length);
            System.arraycopy(bb,0,bs,bh.length,bb.length) ;
        }else {
            bs = responseImplements.getWrite();
        }
        SocketChannel channel = (SocketChannel) key.channel();
        int size = bs.length;
        int wr = write(channel,bs);
        int l = size - wr;
        if (l==0){
            try {
                key.cancel();
                channel.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }else {
            try {

                byte[] bl = new byte[l];
                System.arraycopy(bs,l,bl,0,bl.length);
                SelectionKey newk= channel.register(this.selector,SelectionKey.OP_WRITE);
                responseImplements.setWrite(bl);
                newk.attach(responseImplements);
            }catch (Exception e){
                e.printStackTrace();
            }

        }
    }

    public void writeKey(SelectionKey key)  {

      writeK(key);
    }
    public void register(){
       HttpTask task = blockingQueue.poll();
       if (task == null){
           return;
       }

     SocketChannel cl = task.getSocket();
       if (cl.isOpen()){
           try {
               cl.configureBlocking(false);
            //   cl.setOption(StandardSocketOptions.SO_RCVBUF, 10);
               cl.register(this.selector,SelectionKey.OP_READ,task);
           }catch (ClosedChannelException exception){

               closeChannel(cl);
           } catch (IOException e) {
               throw new RuntimeException(e);
           } finally {
               try {

                   int i =  this.selector.selectNow();

               }catch (IOException e){

               }catch (ClosedSelectorException e){

               }

           }
       }
    }
    public void selected() {
        try {
          //  Selector selector1 = this.selector;
            while (true){
                int var = 0;

                if (!this.blockingQueue.isEmpty()){
                   if (this.selector.selectNow()!=0){
                       break;
                   }

                }
             int keyNum = this.selector.select(10);

               if (keyNum!= 0 || !this.blockingQueue.isEmpty()){
                  break;
                }
                if (Thread.interrupted()){
                    System.out.println("线程被中断");
                    break;
                }

            }
        }catch(IOException | CancelledKeyException e){

            e.printStackTrace();
        }
    }
    void closeChannel(SocketChannel socketChannel){
        try {
            socketChannel.close();
        }catch (IOException exception){

        }

    }
    @Override
    public void init() {
        try {
           // this.contextImp.init();
          //  this.servletAppWeb.init();
            System.out.println("工作者初始化中..");
            this.malloc.init();
            this.selector = Selector.open();

        }catch (IOException e){
            e.printStackTrace();
        }

    }
    @Override
    public void pause() {

    }

    private int write(SocketChannel socketChannel,byte[] bs){
        ByteBuffer byteBr = ByteBuffer.allocate(bs.length);
        byteBr.put(bs);
        byteBr.flip();
        int size= 0;
        try {
          size =  socketChannel.write(byteBr);
        }catch (Exception exception){

        }

        return size;
    }
}
