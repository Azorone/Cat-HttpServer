package org.cathttp.base.net;

import org.cathttp.Task;

import java.nio.channels.SocketChannel;
import java.util.concurrent.atomic.AtomicInteger;

public class AbstractTask implements Task {
    private AtomicInteger status;

    private SocketChannel socketChannel;
    public AbstractTask(){};
    public AbstractTask(SocketChannel socketChannel, TaskStatus status){
        this.socketChannel = socketChannel;
        this.status = new AtomicInteger(status.value);
    }

    public AbstractTask(TaskStatus status){
        this.status = new AtomicInteger(status.value);
    }

    public boolean setStatus(TaskStatus newStatus,TaskStatus oldStatus) {

       return this.status.compareAndSet(oldStatus.value, newStatus.value);

    }
    public int getStatus(){
        return this.status.get();
    }

    public SocketChannel getSocketChannel() {
        return socketChannel;
    }
}
