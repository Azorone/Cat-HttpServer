package org.cathttp.javaee.response;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import java.io.IOException;

public class ServletOutStreamImp extends ServletOutputStream {

    byte[] bs = new byte[1024*1024];
    int position = 0;
    int limit = 1024*1024;
    @Override
    public boolean isReady() {
        return false;
    }

    @Override
    public void setWriteListener(WriteListener writeListener) {

    }

    @Override
    public void write(int b) throws IOException {
         if (position==limit-1){
             System.out.println("缓冲区满");
         }
        bs[position++] = (byte) b;

    }

  public   byte[] getByte(){
        byte[] b = new byte[position];
        System.arraycopy(bs,0,b,0,position);
        position = 0;
        return b;
    }

}
