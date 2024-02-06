package org.cathttp.global;

import org.cathttp.async.threadpool.BusinessThreadPool;
import org.cathttp.base.net.inter.LifeCycle;

public class GlobalConfig implements LifeCycle {

    public BusinessThreadPool businessThreadPool = BusinessThreadPool.getBusinessThreadPool();
    public static GlobalConfig globalConfig = new GlobalConfig();


    private GlobalConfig(){}

    public static GlobalConfig getGlobalConfig() {
        return globalConfig;
    }

    @Override
    public void init() {
        businessThreadPool.init();
    }

    @Override
    public void pause() {

    }
}
