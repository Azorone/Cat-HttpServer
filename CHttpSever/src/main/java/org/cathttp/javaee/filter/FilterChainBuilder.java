package org.cathttp.javaee.filter;

import org.cathttp.javaee.context.ServletContextImp;

import javax.servlet.*;
import java.io.IOException;

public class FilterChainBuilder {

    FilterChainImplement filterChainImplement;
    FilterChainImplement HEAD = new FilterChainImplement(new Filter() {
        @Override
        public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
            filterChain.doFilter(servletRequest,servletResponse);
        }
    });

    public FilterChainBuilder(){
        filterChainImplement = HEAD;
    }
 public    FilterChainBuilder addFilter(FilterProxy proxy){

      this.filterChainImplement =  filterChainImplement.addFilter(proxy);

        return this;
    }

    public FilterChainImplement getFilterChainImplement() {
        return HEAD;
    }
    public void end(ServletContextImp contextImp){
        this.filterChainImplement.build(contextImp);
    }

    public FilterChainImplement getHEAD() {
        return HEAD;
    }
}
