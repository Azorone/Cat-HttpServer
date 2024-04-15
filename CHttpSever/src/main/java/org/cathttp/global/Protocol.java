package org.cathttp.global;

public enum Protocol {
        HTTP1("HTTP/1.1");
        String protocol;
        Protocol(String protocol){
            this.protocol = protocol;
        }

    public String getProtocol() {
        return protocol;
    }
}
