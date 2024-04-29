package org.cathttp.base.buffer;

import java.nio.channels.SocketChannel;

public interface Message {

    int fill(byte[] bytes);  //填充这个消息

    int free();
    byte[] getByte();

    int getIndex();

    long reduceSize(long size);
    long increaseSize(long size);
    boolean messageToSocket(SocketChannel socketChannel,boolean free);
    boolean socketToMessage(SocketChannel socketChannel);
    long getMaxSize();
}
