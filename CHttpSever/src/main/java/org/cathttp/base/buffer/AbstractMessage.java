package org.cathttp.base.buffer;

import java.nio.channels.SocketChannel;

public abstract class AbstractMessage  implements Message{

    int index; //索引,内存节点的索引
    int position = 0; //相比left 的偏移 使用的位置
    boolean end;
    int page;  //单个页大小
    int left;
    int right;
    long written = 0;
    long useSize;
    long size; //  (right - left) * page
    Malloc malloc; //所属对象池
    /*****
     * 把Bytes 填充到 index对应的地址 size = bytes.length position = index + size%page position <= limit
     * *******/
    @Override
    public int fill(byte[] bytes) {
        int size_b  = bytes.length;

        int needPage = (page + size_b -1) /page;
        if (size_b > size){
            System.out.println("溢出");
            return  -1;
        }
        if (left+position > right){
            System.out.println("错误，界限错误");
            return -1;
        }
        if (left+position+needPage-1 > right){
            System.out.println("错误，页不足");
            return -1;
        }
        this.position = malloc.put(bytes,size_b,index,needPage,position);
        this.useSize = this.useSize + size_b;
        
        return 0;
    }

    @Override
    public byte[] getByte() {

        return malloc.getCopy(index,position,useSize);
    }

    @Override
    public int getIndex() {
        return index;
    }

    @Override
    public long reduceSize(long size) {
        this.useSize = this.useSize - size;
        return this.useSize;
    }

    @Override
    public long increaseSize(long size) {
        this.useSize = this.useSize + size;
        return this.useSize;
    }


    @Override
    public boolean messageToSocket(SocketChannel socketChannel, boolean free) {

        int writePage = (int) (this.written  /page);
        int r = position -writePage ;
        long s =  malloc.write(socketChannel,left+writePage,r);
        if (s==0){
            return true;
        }
      if (reduceSize(s) <= 0){
          this.written = 0;
          this.position = 0;
          return true;
      }
     /* try {
            Thread.sleep(1000);
        }catch (Exception e){
          e.printStackTrace();
        }*/
       this.written = this.written + s;
       return false;
    }

    @Override
    public boolean socketToMessage(SocketChannel socketChannel) {
        return false;
    }

    @Override
    public long getMaxSize() {
        return size;
    }

    @Override
    public int free() {
        malloc.free(this);
        this.end = true;
        return 0;
    }
}
