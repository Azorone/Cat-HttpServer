package org.cathttp.javaee.filter;

import javax.servlet.*;
import java.io.IOException;
import java.util.ArrayList;

public class FilterChainImplement implements FilterChain {
    FilterProxy filterProxy;
    FilterChainImplement  nextFilter;
    boolean end = false;
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse) throws IOException, ServletException {

            if (end){
                System.out.println("结束..");
                return;
            }
            System.out.println("下一个过滤器");
            if (filterProxy.isIntercept()){

                System.out.println("符合拦截条件");
                filterProxy.curFilter.doFilter(servletRequest,servletResponse,nextFilter);

            }else {
                    System.out.println("拦截器没执行完毕，执行下一个拦截器");
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

}
