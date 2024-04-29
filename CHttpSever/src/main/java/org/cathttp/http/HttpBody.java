package org.cathttp.http;

public class HttpBody {
    byte[] bs;
    public HttpBody(byte[] bs){
        this.bs = bs;
    }

    public void setBs(byte[] bs) {
        this.bs = bs;
    }

    public HttpBody(){}

    public byte[] getBs() {
        return bs;
    }
}
