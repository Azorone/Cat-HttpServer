package org.cathttp.base.net;

import org.cathttp.base.net.inter.LifeCycle;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;

public class Accept extends Thread implements LifeCycle {

    Selector selector;
    private int port;
    TaskDistributionCenter taskDistributionCenter;
    private ServerSocketChannel socketChannel;

    private boolean run = true;
    public Accept(int port){
        this.port = port;
    }

    @Override
    public void run() {
        try {
            System.out.println("Accept初始化成功...");
            while (run){
                this.selector.select(500);
                Set<SelectionKey> selectionKeys = this.selector.selectedKeys();
                Iterator<SelectionKey> selectionKeyIterator = selectionKeys.iterator();
                while (selectionKeyIterator.hasNext()){
                    SelectionKey socketChannel1 = selectionKeyIterator.next();
                    if (socketChannel1.isAcceptable()){
                     ServerSocketChannel serverSocketChannel = (ServerSocketChannel) socketChannel1.channel();
                     SocketChannel socketChannel2= serverSocketChannel.accept();
                    if (socketChannel2!=null){
                        taskDistributionCenter.input(socketChannel2);
                      }
                    }
                    selectionKeyIterator.remove();
                }
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    @Override
    public void init() {
        try {
            System.out.println("Accept初始化中...");
            taskDistributionCenter = new TaskDistributionCenter(new Worker[6], new LinkedBlockingDeque<>());
            taskDistributionCenter.init();
            this.selector = Selector.open();
            socketChannel = ServerSocketChannel.open();
            socketChannel.bind(new InetSocketAddress(port));

            socketChannel.configureBlocking(false);
            socketChannel.register(this.selector,SelectionKey.OP_ACCEPT);

            Thread thread = new Thread(taskDistributionCenter);
            thread.start();

        }catch (IOException e){
            e.printStackTrace();
            return;
        }
    }

    @Override
    public void pause() {
        this.run = false;
    }
}
