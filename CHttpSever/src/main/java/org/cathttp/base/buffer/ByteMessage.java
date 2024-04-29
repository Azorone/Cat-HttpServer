package org.cathttp.base.buffer;

import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

public class ByteMessage extends AbstractMessage{
    ByteMessage(BuddyMalloc.Index indexObject,int pageSize,Malloc malloc,long size,int index){
        this.index    =  index;
        this.position = 0;
        this.left = indexObject.left;
        this.right = indexObject.right;
        this.size = size;
        this.page = pageSize;
        this.useSize = 0;
        this.malloc  = malloc;
        this.end     =false;
    }



}
