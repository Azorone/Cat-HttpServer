package org.cathttp.global;

import org.cathttp.base.net.BusinessThreadPool;
import org.cathttp.base.net.inter.LifeCycle;

import javax.servlet.ServletConfig;

public class GlobalConfig implements LifeCycle {

    public BusinessThreadPool businessThreadPool = BusinessThreadPool.getBusinessThreadPool();
    public static GlobalConfig globalConfig = new GlobalConfig();
    public static ServletConfig servletConfig;

    private Protocol protocol = Protocol.HTTP1;
    private GlobalConfig(){}
    public static GlobalConfig getGlobalConfig() {
        return globalConfig;
    }
    private GlobalConfig setProtocol(Protocol protocol){
        this.protocol = protocol;
        return this;
    }
    @Override
    public void init() {
        businessThreadPool.init();
    }

    @Override
    public void pause() {

    }
}
