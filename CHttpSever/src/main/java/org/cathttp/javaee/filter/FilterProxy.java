package org.cathttp.javaee.filter;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;

//过滤器顺序有关
public class FilterProxy {
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

    boolean isIntercept(){

        return true;
    }


}
