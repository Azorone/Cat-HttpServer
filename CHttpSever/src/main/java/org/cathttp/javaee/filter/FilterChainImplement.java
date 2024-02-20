package org.cathttp.javaee.filter;

import org.cathttp.javaee.context.ServletContextImp;

import javax.servlet.*;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;

public class FilterChainImplement implements FilterChain {
    FilterProxy filterProxy;
    FilterChainImplement  nextFilter;
    boolean end = false;
    ServletContextImp servletContextImp = ServletContextImp.getServletContext();
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse) throws IOException, ServletException {
             HttpServletRequest req = (HttpServletRequest) servletRequest;
            if (end){
                System.out.println("过滤器链条结束..进入Servlet 分发");
                if (servletContextImp!=null){
                   RequestDispatcher s = servletContextImp.getRequestDispatcher("/");
                   if (s!=null){
                       s.forward(servletRequest,servletResponse);
                   }
                   System.out.println("没拦截");
                }
                return;
            }
            System.out.println("下一个过滤器");

            if ( filterProxy.isIntercept(req.getRequestURI())){

                System.out.println("符合拦截条件");
                filterProxy.curFilter.doFilter(servletRequest,servletResponse,nextFilter);

            }else {
                    System.out.println("该拦截器不符合拦截条件，判断下一个拦截器是否符合");
                    nextFilter.doFilter(servletRequest,servletResponse);
            }
    }

    public FilterChainImplement(){};

    public FilterChainImplement(Filter filter){
        FilterProxy filterProxy1 = new FilterProxy();
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
