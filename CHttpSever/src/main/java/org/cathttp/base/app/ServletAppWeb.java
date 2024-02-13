package org.cathttp.base.app;

import org.cathttp.base.net.inter.LifeCycle;
import org.cathttp.base.servlet.ServletProxy;

import javax.servlet.FilterChain;
import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpSession;
import java.util.Map;


public class ServletAppWeb  implements LifeCycle {
    ServletContext servletContext;

    FilterChain filterChain;
    HttpSession session;
    Map<String, ServletProxy> servletProxyMap;

    ServletAppWeb(ServletContext servletContext,FilterChain filterChain,Map<String,ServletProxy> map){

        this.filterChain = filterChain;
        this.servletContext = servletContext;
        this.servletProxyMap = map;

    };

    @Override
    public void init() {

    }

    @Override
    public void pause() {

    }
}
