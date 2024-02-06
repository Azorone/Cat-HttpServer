package org.cathttp;

import org.cathttp.base.net.Accept;
import org.cathttp.base.net.inter.LifeCycle;
import org.cathttp.global.GlobalConfig;

public class Server implements LifeCycle {
    Accept accept;
    int port;
    public Server(int port){
        this.port = port;
        init();
    }
    @Override
    public void init() {
        System.out.println("Hello world!");
        GlobalConfig config = GlobalConfig.getGlobalConfig();
        config.init();
        accept = new Accept(port);
        accept.init();
        accept.start();
    }

    @Override
    public void pause() {

    }
}
