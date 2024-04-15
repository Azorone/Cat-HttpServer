package org.cathttp.javaee.filter;

import org.cathttp.base.app.StaticAppWeb;
import org.cathttp.javaee.context.ServletContextImp;

import javax.servlet.*;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class FilterChainImplement implements FilterChain {
    FilterProxy filterProxy;
    FilterChainImplement  nextFilter;
    boolean end = false;
    ServletContextImp servletContextImp = ServletContextImp.getServletContext();
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse) throws IOException, ServletException {
             HttpServletRequest req = (HttpServletRequest) servletRequest;
            if (end){
                if (servletContextImp!=null){
                    if (StaticAppWeb.getStaticAppWeb().getStaticFile(req, (HttpServletResponse) servletResponse)){
                        return;
                    }
                   RequestDispatcher s = servletContextImp.getRequestDispatcher(req.getRequestURI());
                   if (s!=null){
                       s.forward(servletRequest,servletResponse);
                   }else {

                       ((HttpServletResponse) servletResponse).setStatus(404);
                   }
                }
                return;
            }
            if ( filterProxy.isIntercept(req.getRequestURI())){

                filterProxy.curFilter.doFilter(servletRequest,servletResponse,nextFilter);
            }else {
                    nextFilter.doFilter(servletRequest,servletResponse);
            }
    }

    public FilterChainImplement(){};

    public FilterChainImplement(Filter filter){
        FilterProxy filterProxy1 = new FilterProxy();
        Set<String> strings = new HashSet<>();
        strings.add("/");
        filterProxy1.urlPatterns = strings;
        filterProxy1.curFilter = filter;
        FilterChainImplement filterChainImplement = new FilterChainImplement();
        this.filterProxy = filterProxy1;

    }

    public FilterChainImplement addFilter(Filter filter){

        FilterProxy filterProxy1 = new FilterProxy();
        filterProxy1.curFilter = filter;
        FilterChainImplement filterChainImplement = new FilterChainImplement();
        filterChainImplement.filterProxy = filterProxy1;
        this.nextFilter = filterChainImplement;
        return filterChainImplement;
    }
    public FilterChainImplement addFilter(FilterProxy filterProxy){
        FilterChainImplement filterChainImplement = new FilterChainImplement();
        filterChainImplement.filterProxy = filterProxy;
        this.nextFilter = filterChainImplement;
        return filterChainImplement;
    }
    public void build(){
        this.nextFilter = new FilterChainImplement();
        nextFilter.end = true;
    }

    public void build(ServletContextImp servletContext){
        this.servletContextImp = servletContext;
        this.nextFilter = new FilterChainImplement();
        nextFilter.end = true;
    }
}
