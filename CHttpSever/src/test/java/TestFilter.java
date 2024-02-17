import org.cathttp.javaee.filter.FilterChainImplement;
import org.cathttp.javaee.request.ServletRequestImplement;
import org.cathttp.javaee.response.ServletResponseImplements;
import org.junit.Test;

import javax.servlet.*;
import java.io.IOException;

/****
 * 测试过滤器
 * 1.异常抛出测试
 * 2.流程执行测试
 * **/
public class TestFilter {
    /**
     * 2.流程执行测试
     * **/
    @Test
    public void TestFilterChain() throws ServletException, IOException {

        FilterChainImplement filterChainImplement = new FilterChainImplement(new Filter() {
            @Override
            public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
                System.out.println("0号");
                filterChain.doFilter(servletRequest,servletResponse);
                System.out.println("1-a");
            }
        });
        Filter filter = new Filter() {
            @Override
            public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
                System.out.println("1号");
                filterChain.doFilter(servletRequest,servletResponse);
                System.out.println("2-a");
            }
        };
        Filter filter1 = new Filter() {
            @Override
            public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
                System.out.println("2号");
                filterChain.doFilter(servletRequest,servletResponse);
                System.out.println("3-a");
            }
        };
        filterChainImplement.addFilter(filter).addFilter(filter1).build();
        filterChainImplement.doFilter(new ServletRequestImplement(),new ServletResponseImplements());
    }
}
