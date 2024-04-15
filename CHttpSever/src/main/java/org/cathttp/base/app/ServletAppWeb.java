package org.cathttp.base.app;

import org.cathttp.base.net.inter.LifeCycle;
import org.cathttp.javaee.context.ServletContextImp;
import org.cathttp.javaee.filter.FilterChainBuilder;
import org.cathttp.javaee.filter.FilterChainImplement;
import org.cathttp.javaee.filter.FilterProxy;
import org.cathttp.javaee.request.ServletRequestImplement;
import org.cathttp.javaee.servlet.ServletProxy;
import org.cathttp.javaee.session.SessionImp;
import org.cathttp.javaee.session.SessionManger;
import org.cathttp.loaded.PackLoader;
import org.cathttp.loaded.StaticLoader;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;


public class ServletAppWeb  implements LifeCycle {
   private static final ServletContext servletContext = ServletContextImp.getServletContext();

   private static  volatile ServletAppWeb instance = new ServletAppWeb();

   String[] packs;
    public static ServletAppWeb getInstance(String... pack) {
        instance.packs= pack;
        return instance;
    }
    public static ServletAppWeb getInstance() {

        return instance;
    }
    private static final SessionManger sessionManger = SessionManger.getInstanceSession();
   private static  FilterChainImplement filterChainImplement;

    private ServletAppWeb(String... pack){

    };

    public void loadPack(String... pack){

        if (pack.length==0){return;}
        ServletContextImp servletContextImp = ServletContextImp.getServletContext();

        PackLoader loader = new PackLoader(pack);
        loader.init();
        ArrayList<ServletProxy> servletProxies  = loader.getHttpServlets();
        ArrayList<FilterProxy> filterProxies   = loader.getFilterProxies();
        FilterChainBuilder filterChainBuilder  = new FilterChainBuilder();
        for (FilterProxy fp:filterProxies){
            fp.init();
            filterChainBuilder.addFilter(fp);
        }
        filterChainBuilder.end(servletContextImp);
        filterChainImplement = filterChainBuilder.getHEAD();
        for (ServletProxy sp:servletProxies){
            sp.getServlet();
            sp.init();
        }
        servletContextImp.setServletProxies(servletProxies);
    }

    public void run(ServletRequest httpServletRequest, ServletResponse httpServletResponse) throws ServletException, IOException {
        //HttpServletRequest request = (HttpServletRequest) httpServletRequest;
        //HttpServletResponse response = (HttpServletResponse) httpServletResponse;
        //File file = staticLoader.get(request.getRequestURI());

        ServletRequestImplement requestImplement = (ServletRequestImplement) httpServletRequest;
        Cookie[] cookies = requestImplement.getCookies();
        if (cookies==null){

         SessionImp sessionImp = sessionManger.creatSession((HttpServletResponse) httpServletResponse);
         requestImplement.setSession(sessionImp);
        }else {
            boolean has = false;
            for (Cookie cookie:cookies){
                if (cookie.getName().equals("JSESSIONID")){

                  HttpSession session =  sessionManger.getSession(cookie.getValue());
                  if (session==null){
                      SessionImp sessionImp = sessionManger.creatSession((HttpServletResponse) httpServletResponse);
                      requestImplement.setSession(sessionImp);
                  }else {

                      requestImplement.setSession(session );

                  }

                has = true;
                break;
                }
            }
            if (!has){
                SessionImp sessionImp = sessionManger.creatSession((HttpServletResponse) httpServletResponse);
                requestImplement.setSession(sessionImp);
            }
        }
        //if (file==null){
            filterChainImplement.doFilter(httpServletRequest,httpServletResponse);
      //  }else {
        //    FileInputStream fileInputStream = new FileInputStream(file);
          //  byte[] bs = new byte[fileInputStream.available()] ;
           // fileInputStream.read(bs);
            //response.getOutputStream().write(bs);
           // response.setContentType("text/html; charset=utf-8");
            //fileInputStream.close();
      //  }
    }


    @Override
    public void init() {

        if (instance.packs!=null){
            System.out.println("ServletAppWeb加载中...");
            instance.loadPack(packs);
        }


    }

    @Override
    public void pause() {
        instance = null;
    }
}
