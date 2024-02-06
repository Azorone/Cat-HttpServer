package org.cathttp.base.net;

import java.nio.channels.SocketChannel;

public class HttpTask extends AbstractTask {



  public  HttpTask(){

    }
   public HttpTask(SocketChannel socketChannel, TaskStatus status){
        super(socketChannel,status);
    }
    HttpTask(TaskStatus status){
        super(status);
    }

    SocketChannel getSocket(){
        return this.getSocketChannel();
    }
}
