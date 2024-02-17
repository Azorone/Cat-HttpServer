package org.cathttp.javaee.servlet;

import org.cathttp.base.net.inter.LifeCycle;
import org.cathttp.javaee.request.ServletRequestImplement;

import javax.servlet.*;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import java.io.IOException;
import java.util.*;

public class ServletProxy  implements LifeCycle {

    @Override
    public void init() {

    }

    @Override
    public void pause() {

    }

    HttpServlet servlet = null;
    Set<String> urlPatterns = null;
    String name = null;
    String[] value = null;
    String description="";
    boolean asyncSupported=false;
    int loadOnStartup=-1;
    String smallIcon="";
    String largeIcon="";
    WebInitParam[] initParams=null;
    WebServlet webServlet=null;
    String displayName="";
    public ServletProxy(){};
    public ServletProxy(WebServlet webServlet,HttpServlet httpServlet){
        this.webServlet = webServlet;
        this.servlet = httpServlet;
        if (webServlet!=null && httpServlet!=null){
            builderHttpServlet();
        }
    }

    private void builderHttpServlet(){

       this.value = webServlet.value();
       this.asyncSupported = webServlet.asyncSupported();
       this.name = webServlet.name();
       urlPatterns = new HashSet<>(List.of(webServlet.urlPatterns()));
       description = webServlet.description();
       displayName =  webServlet.displayName();
       this.loadOnStartup = webServlet.loadOnStartup();
       this.initParams = webServlet.initParams();
       this.smallIcon = webServlet.smallIcon();
       this.largeIcon = webServlet.largeIcon();
        ServletConfig servletConfig = new ServletConfig() {
            @Override
            public String getServletName() {
                return null;
            }

            @Override
            public ServletContext getServletContext() {
                return null;
            }

            @Override
            public String getInitParameter(String s) {
                return null;
            }

            @Override
            public Enumeration<String> getInitParameterNames() {
                return null;
            }
        };
        // servlet.init(servletConfig);
    }
    public void doService(ServletRequestImplement request, ServletResponse response) throws ServletException, IOException {
        if (servlet!=null){
            servlet.service(request,response);
        }
    }
    public HttpServlet getServlet() {
        return servlet;
    }

    public void setServlet(HttpServlet servlet) {
        this.servlet = servlet;
    }

    public Set<String> getUrlPatterns() {
        return urlPatterns;
    }

    public void setUrlPatterns(Set<String> urlPatterns) {
        this.urlPatterns = urlPatterns;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public WebServlet getWebServlet() {
        return webServlet;
    }

    public void setWebServlet(WebServlet webServlet) {
        this.webServlet = webServlet;
    }

    @Override
    public String toString() {
        return "ServletProxy{" +
                "servlet=" + servlet +
                ", urlPatterns=" + urlPatterns +
                ", name='" + name + '\'' +
                ", value=" + Arrays.toString(value) +
                ", description='" + description + '\'' +
                ", asyncSupported=" + asyncSupported +
                ", loadOnStartup=" + loadOnStartup +
                ", smallIcon='" + smallIcon + '\'' +
                ", largeIcon='" + largeIcon + '\'' +
                ", initParams=" + Arrays.toString(initParams) +
                ", webServlet=" + webServlet +
                ", displayName='" + displayName + '\'' +
                '}';
    }
}
