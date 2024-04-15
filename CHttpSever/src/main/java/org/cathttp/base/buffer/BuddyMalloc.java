package org.cathttp.base.buffer;

import org.cathttp.base.net.inter.LifeCycle;

import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class BuddyMalloc implements Malloc {

    private ByteBuffer  buffer[];

    private  final boolean isDirect = true;
    private int pageSize;
    private int allPageSize;
    private Index[] indexBuf;

    private long allSize ;

    @Override
    public void init() {
        if (isDirect){
            directMalloc(pageSize,allPageSize);
        }
    }

    @Override
    public void pause() {
        System.out.println(Thread.currentThread().getName()+"的内存分配器开始销毁...");
        buffer = null;
        indexBuf = null;
        System.gc();
    }
   public BuddyMalloc(){};

   public BuddyMalloc(int pageSize,int allPageSize,boolean isDirect){
          this.pageSize = pageSize;
          this.allPageSize = allPageSize;
          this.allSize = (long) this.allPageSize * this.pageSize;

   };
   private void directMalloc(int pageSize,int pageSizeNums){   //pageSize 页框大小必须是2的整数倍 pageSizeNumes 页框个数 该个数必须是2的整数倍

       long totalSize = (long) pageSize *pageSizeNums;
       System.out.println("内存分配器开始初始化...总容量为"+totalSize);
      if (!is2ofPower(pageSize) && pageSize < 1 && !is2ofPower(pageSize)){
          return;
      }
      this.buffer = new ByteBuffer[pageSizeNums];
      for (int i=0;i<pageSizeNums;i++){   //初始化存储空间
          this.buffer[i] = ByteBuffer.allocateDirect(pageSize);
      }
       indexBuf = new Index[pageSizeNums*2-1];
      for (int i=0;i<pageSizeNums*2 - 1;i++){  //初始化索引
          if (is2ofPower(i+1)&&i!=0){
                totalSize= totalSize/2;
          }
          indexBuf[i] = new Index();
          indexBuf[i].size   = totalSize;
      }
      builderIndex();

   }

   private void builderIndex(){
    int leve = 0;
    for (int i = 0; i< indexBuf.length; i++){
        indexBuf[i].left  = 0;
        indexBuf[i].right = (int) ((allPageSize/ (Math.pow(2,leve)))-1);
        for (int j=1;j<Math.pow(2,leve);j++){
            i++;
            indexBuf[i].left = indexBuf[i-1].right+1;
            indexBuf[i].right = (int) (indexBuf[i].left + (allPageSize/ (Math.pow(2,leve)))-1);
        }
        leve = leve + 1;
    }
   }

   public void showIndex(){
      for (int i = 0; i< indexBuf.length; i++){
          System.out.println("Index["+i+"] "+"{"+ indexBuf[i].left+"<--("+ indexBuf[i].size+")-->"+ indexBuf[i].right+" }");
      }
   }
   class Index{
       int left;
       int right;
       long size;
   }
    @Override
    public Message allocate(int size) {
        int index = alloc(size);
        ByteMessage byteMessage = new ByteMessage(indexBuf[index],this.pageSize,this,fixSize(size),index);
        return byteMessage;
    }

    @Override
    public void free(Message message) {
       free(message.getIndex(),message.getMaxSize());
       Index index = indexBuf[message.getIndex()];
  for (int i= index.left;i<= index.right;i++){
           buffer[i].clear();
       }
    }

    private int free(int index,long size){
       long node_size = size;
       long left_size;
       long right_size;

       if (index > indexBuf.length ){
           System.out.println(index);
           System.out.println("错误访问页");
           return -1;
       }
       for (;this.indexBuf[index].size!=0;index = parent(index)){
           node_size = node_size*2;
           if (index==0){
               return 0;
           }
       }
       this.indexBuf[index].size = node_size;
       while (index > 0){
           index = parent(index);
           node_size = node_size*2;
           left_size = this.indexBuf[left(index)].size;
           right_size = this.indexBuf[right(index)].size;

           if (left_size + right_size == node_size){
               this.indexBuf[index].size = node_size;
           }else {
               this.indexBuf[index].size = Math.max(left_size,right_size);
           }
       }

       return index;
    }
    public void showIndexM(int i){
        System.out.println("Index["+i+"] "+"{"+ indexBuf[i].left+"<--("+ indexBuf[i].size+")-->"+ indexBuf[i].right+" }");
    }
    private int alloc(long size){ //返回 longest中可分配模块的下标

        int  offset = 0; int  index = 0;long node_size;
        if (size<0){return -1;}

        if (size<pageSize){size = pageSize;}
        else if (!is2ofPower(size)){size = fixSize(size);}

        if (size > indexBuf[index].size){return -2;}
        for (node_size = allSize;node_size!=size; node_size/=2){

            if (this.indexBuf[left(index)].size >= size){index = left(index);
            }else {index = right(index);}
        }
        offset = index;
        indexBuf[index].size = 0;
        while (index!=0){
            index = parent(index);
            this.indexBuf[index].size = Math.max(this.indexBuf[left(index)].size,this.indexBuf[right(index)].size);
        }
        return offset;
    }
    @Override
    public long write(SocketChannel socketChannel, int left, int position) {

        long size;
        try {
         size =    socketChannel.write(buffer,left,position);

        }catch (Exception e){

            size = 0;
            e.printStackTrace();
        }
        return size;
    }
   @Override
    public long read(SocketChannel socketChannel, int left,int position) {
        return 0;
    }

    /*******
     * position 相对left的偏移量
     * ******/
   public int put(byte[] bs, int length,int index,int needPage,int position) {
       Index in = this.indexBuf[index];
       int offset = 0;
       for (;position<needPage;position++){
           if (length>=pageSize){

               buffer[in.left+position].put(bs,offset,pageSize);
               length = length - pageSize;
               offset = offset + pageSize;
               buffer[in.left+position].flip();

           }else {
               buffer[in.left+position].put(bs,offset,length);
               buffer[in.left+position].flip();
           }

       }
       return position;
   }

   public byte[] getCopy(int index, int position, long size){
       Index in = this.indexBuf[index];
       int offset = 0;
       byte[] bs = new byte[(int) size];
       for (int i=in.left;i<in.left+position;i++){
           if (size>=pageSize){
               buffer[i].get(bs,offset,pageSize);
               offset = offset + pageSize;
               size = size - pageSize;
               buffer[i].flip();
           }else {

               buffer[i].get(bs,offset, (int) size);
               buffer[i].flip();
           }
       }
       return bs;
   }
   public byte[] getBytes(int index,int position,int size){
        return null;
   }
    public byte[] getCopyFree(int index,int position,int size,long maxSize){
        Index in = this.indexBuf[index];
        int offset = 0;
        byte[] bs = new byte[size];
        for (int i=in.left;i<in.left+position;i++){
            if (size>=pageSize){
                buffer[i].get(bs,offset,pageSize);
                offset = offset + pageSize;
                size = size - pageSize;
                buffer[i].clear();
            }else {

                buffer[i].get(bs,offset,size);
                buffer[i].clear();
            }

        }
        free(index,maxSize);
        return bs;
    }

    //下标索引从零开始
    private int left(int index){
        return   (index<<1) + 1;
    }
    private int right(int index){
        return   (index<<1) + 2;
    }
    private int parent(int index){
        return ((index+1)>>1)- 1;
    }
    private boolean is2ofPower(long n){
        if (n <= 0)
            return false;
        return (n & (n - 1)) == 0;
    }
    private long fixSize(long size){
        size |= size >> 1;
        size |= size >> 2;
        size |= size >> 4;
        size |= size >> 8;
        size |= size >> 16;
        size |= size >> 32;
        return size+1;
    }


}
