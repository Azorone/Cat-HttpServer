package org.cathttp.base.net;

import org.cathttp.async.threadpool.BusinessThreadPool;
import org.cathttp.base.net.inter.LifeCycle;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.BlockingQueue;

public class Worker extends Thread implements LifeCycle {

   private Selector selector;

   private final BusinessThreadPool businessThreadPool = BusinessThreadPool.getBusinessThreadPool();
   private Selector selectorNew; //用于空轮询重建
    BlockingQueue<HttpTask> blockingQueue;
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

                while (true){
                       this.selected();
                        register();
                        runKey();
                }
    }
    public boolean runKey(){
        System.out.println("runKey");
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
      try {
          businessThreadPool.submit(new Runnable() {
              @Override
              public void run() {
                  if (key.isValid()){
                      SocketChannel socketChannel = (SocketChannel) key.channel();
                      if (socketChannel.isOpen()&&socketChannel.isConnected()){

                          ByteBuffer buffer = ByteBuffer.allocate(1024*4);
                          try {
                              socketChannel.read(buffer);
                              buffer.flip();
                              byte []s = buffer.array();
                              System.out.println("接收内容"+new String(s, StandardCharsets.UTF_8));
                              key.interestOps(SelectionKey.OP_WRITE);
                          }catch (IOException e){
                              System.out.println("读异常");
                          }
                      }else {
                          try {
                              socketChannel.close();
                              key.cancel();
                          }catch (IOException ioException){
                              System.out.println("读时对象被断开");
                          }

                      }

                  }
              }
          });
      }catch (InterruptedException exception){
          System.out.println("线程终止");
      }
    }
    public void writeKey(SelectionKey key){
        System.out.println("写");
        String httpResponse = "HTTP/1.1 200 OK\r\n" +
                "Content-Length: 38\r\n" +
                "Content-Type: text/html\r\n" +
                "\r\n" +
                "<html><body>Hello World!</body></html>";

        SocketChannel socketChannel = (SocketChannel) key.channel();
        if (socketChannel.isConnected()&&socketChannel.isOpen()){
           ByteBuffer buffer = ByteBuffer.allocate(httpResponse.length());

           try {
               buffer.put(httpResponse.getBytes());
               buffer.flip();
              if(socketChannel.write(buffer)>0){
                  System.out.println("写完");
              };
           }catch (Exception e){
               System.out.println("写异常");
           }

        }
        key.interestOps(0);
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
            Selector selector1 = this.selector;
            while (true){
                int var = 0;

                if (!this.blockingQueue.isEmpty()){
                    this.selector.selectNow();
                    break;
                }
               int keyNum = this.selector.select(500);

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
            this.selector = Selector.open();
        }catch (IOException e){
            e.printStackTrace();
        }

    }
    @Override
    public void pause() {

    }
}
