import org.cathttp.javaee.context.ServletContextImp;
import org.cathttp.javaee.filter.FilterProxy;
import org.cathttp.javaee.request.ServletRequestImplement;
import org.cathttp.javaee.response.ServletResponseImplements;
import org.cathttp.javaee.servlet.ServletProxy;
import org.cathttp.loaded.PackLoader;
import org.junit.Test;

import java.util.ArrayList;

public class TestContext {
    /*****
     * 简单测试拦截器与Servlet是否协同工作
     * ****/
    @Test
    public void testContext(){

        ServletContextImp servletContextImp = ServletContextImp.getServletContext();

        PackLoader loader = new PackLoader("testF","testS");
        loader.init();
        ArrayList<ServletProxy> S  = loader.getHttpServlets();
        ArrayList<FilterProxy> F   = loader.getFilterProxies();

        for (ServletProxy s:S){
           System.out.println(s.getName());
        }
        for (FilterProxy f:F){
            System.out.println(f.getFilterName());
        }
        servletContextImp.setServletProxies(S);
        servletContextImp.setFilterProxies(F);
        servletContextImp.init0();
        servletContextImp.run(new ServletRequestImplement(),new ServletResponseImplements());
    }
}
