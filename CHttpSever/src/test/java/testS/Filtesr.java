package testS;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;
@WebFilter(urlPatterns = "/test",filterName = "ces1")
public class Filtesr implements javax.servlet.Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        javax.servlet.Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("UES");
        filterChain.doFilter(servletRequest,servletResponse);
    }

    @Override
    public void destroy() {
        javax.servlet.Filter.super.destroy();
    }
}
