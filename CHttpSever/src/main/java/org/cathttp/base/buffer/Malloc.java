package org.cathttp.base.buffer;

import org.cathttp.base.net.inter.LifeCycle;

import java.nio.channels.SocketChannel;

public interface Malloc extends LifeCycle {

    Message  allocate(int size);
    long write(SocketChannel socketChannel, int left, int position);  //把消息写入Socket
    long read(SocketChannel socketChannel,int left,int position);   //从Socket读消息
    /*****
     * 把bs 填充到 index 起始，position结束的地方
     *
     * *******/

    int put(byte[] bs, int length,int index,int needPage,int position);
    void free(Message message);
    byte[] getCopy(int index,int position,long size);

}
