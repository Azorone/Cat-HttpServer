package org.cathttp.javaee.filter;

import org.cathttp.base.net.inter.LifeCycle;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;

//过滤器顺序有关
public class FilterProxy implements LifeCycle {
    @Override
    public void init() {

    }

    @Override
    public void pause() {

    }

    Filter curFilter;
    String description;
    String displayName;
    WebInitParam[] initParams;
    String filterName;
    String smallIcon;
    String largeIcon;
    String[] servletNames;
    String[] value;
    String[] urlPatterns;
    DispatcherType[] dispatcherTypes;
    boolean asyncSupported;

    public FilterProxy(WebFilter webFilter){

    }
    public FilterProxy(){};
    boolean isIntercept(){

        return true;
    }

    public Filter getCurFilter() {
        return curFilter;
    }
}
