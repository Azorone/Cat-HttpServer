package org.cathttp.javaee.context;

import org.cathttp.base.net.inter.LifeCycle;
import org.cathttp.javaee.filter.FilterChainBuilder;
import org.cathttp.javaee.filter.FilterChainImplement;
import org.cathttp.javaee.filter.FilterProxy;
import org.cathttp.javaee.servlet.ServletProxy;

import javax.servlet.*;
import javax.servlet.descriptor.JspConfigDescriptor;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSessionListener;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ServletContextImp implements javax.servlet.ServletContext, LifeCycle {

    ArrayList<ServletProxy> servletProxies = new ArrayList<>();
    ArrayList<FilterProxy> filterProxies   = new ArrayList<>();
    FilterChainImplement filterChainImplement;
    private static ServletContextImp servletContext = new ServletContextImp();
    ConcurrentHashMap<String,Object> Attribute = new ConcurrentHashMap<>();


    int MajorVersion = 0;

    String contextPath;

    private ServletContextImp(){};

    public static ServletContextImp getServletContext() {
        return servletContext;
    }

    public void mapperServlet(HttpServletRequest request, HttpServletResponse response){

    }

    public ArrayList<ServletProxy> getServletProxies() {
        return servletProxies;
    }

    public void setServletProxies(ArrayList<ServletProxy> servletProxies) {
        this.servletProxies = servletProxies;
    }

    public ArrayList<FilterProxy> getFilterProxies() {
        return filterProxies;
    }

    public void setFilterProxies(ArrayList<FilterProxy> filterProxies) {
        this.filterProxies = filterProxies;
    }

    public FilterChainImplement getFilterChainImplement() {
        return filterChainImplement;
    }

    public void setFilterChainImplement(FilterChainImplement filterChainImplement) {
        this.filterChainImplement = filterChainImplement;
    }

    private void sortFilter(){
        filterProxies.sort(new Comparator<FilterProxy>() {
            @Override
            public int compare(FilterProxy o1, FilterProxy o2) {
                return 0;
            }
        });
    }
    private void sortServlet(){
        servletProxies.sort(new Comparator<ServletProxy>() {
            @Override
            public int compare(ServletProxy o1, ServletProxy o2) {
                return 0;
            }
        });
    }

    public void run(HttpServletRequest httpServletRequest,HttpServletResponse response){

        try {
            filterChainImplement.doFilter(httpServletRequest,response);
        }catch (Exception E){
            E.printStackTrace();
        }

    }

    public void  init0(){
     /*   this.filterChainImplement = new FilterChainImplement(new Filter() {
            @Override
            public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
                filterChain.doFilter(servletRequest,servletResponse);
            }
        });*/
        /******
         * BUG 应使用链表
         * ******/

    }



    @Override
    public void init() {
            init0();
    }

    @Override
    public void pause() {

    }

    @Override
    public String getContextPath() {
        return contextPath;
    }

    @Override
    public ServletContext getContext(String s) {
        return servletContext;
    }

    @Override
    public int getMajorVersion() {
        return MajorVersion;
    }

    @Override
    public int getMinorVersion() {
        return 0;
    }

    @Override
    public int getEffectiveMajorVersion() {
        return 0;
    }

    @Override
    public int getEffectiveMinorVersion() {
        return 0;
    }
    //获取文件的MimeType
    @Override
    public String getMimeType(String s) {
        return null;
    }
    //或取s下的资源目录
    @Override
    public Set<String> getResourcePaths(String s) {
        return null;
    }

    @Override
    public URL getResource(String s) throws MalformedURLException {
        return null;
    }
    //获取s资源文件的输入流
    @Override
    public InputStream getResourceAsStream(String s) {
        return null;
    }
    //
    @Override
    public RequestDispatcher getRequestDispatcher(String s) {
           for (ServletProxy proxy:servletProxies){
              if (proxy.getUrlPatterns().contains(s)){
                  return proxy;
              }
           }
        return null;
    }



    @Override
    public RequestDispatcher getNamedDispatcher(String s) {
        return null;
    }
    //
    @Override
    public Servlet getServlet(String s) throws ServletException {
        return null;
    }

    @Override
    public Enumeration<Servlet> getServlets() {
        return null;
    }

    @Override
    public Enumeration<String> getServletNames() {
        return null;
    }

    @Override
    public void log(String s) {

    }

    @Override
    public void log(Exception e, String s) {

    }

    @Override
    public void log(String s, Throwable throwable) {

    }
    //获取真实路径
    @Override
    public String getRealPath(String s) {
        return null;
    }

    @Override
    public String getServerInfo() {
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

    @Override
    public boolean setInitParameter(String s, String s1) {
        return false;
    }

    @Override
    public Object getAttribute(String s) {
        return null;
    }

    @Override
    public Enumeration<String> getAttributeNames() {
        return null;
    }

    @Override
    public void setAttribute(String s, Object o) {

    }

    @Override
    public void removeAttribute(String s) {

    }

    @Override
    public String getServletContextName() {
        return null;
    }

    @Override
    public ServletRegistration.Dynamic addServlet(String s, String s1) {
        return null;
    }

    @Override
    public ServletRegistration.Dynamic addServlet(String s, Servlet servlet) {
        return null;
    }

    @Override
    public ServletRegistration.Dynamic addServlet(String s, Class<? extends Servlet> aClass) {
        return null;
    }

    @Override
    public ServletRegistration.Dynamic addJspFile(String s, String s1) {
        return null;
    }

    @Override
    public <T extends Servlet> T createServlet(Class<T> aClass) throws ServletException {
        return null;
    }

    @Override
    public ServletRegistration getServletRegistration(String s) {
        return null;
    }

    @Override
    public Map<String, ? extends ServletRegistration> getServletRegistrations() {
        return null;
    }

    @Override
    public FilterRegistration.Dynamic addFilter(String s, String s1) {
        return null;
    }

    @Override
    public FilterRegistration.Dynamic addFilter(String s, Filter filter) {
        return null;
    }

    @Override
    public FilterRegistration.Dynamic addFilter(String s, Class<? extends Filter> aClass) {
        return null;
    }

    @Override
    public <T extends Filter> T createFilter(Class<T> aClass) throws ServletException {
        return null;
    }

    @Override
    public FilterRegistration getFilterRegistration(String s) {
        return null;
    }

    @Override
    public Map<String, ? extends FilterRegistration> getFilterRegistrations() {
        return null;
    }

    @Override
    public SessionCookieConfig getSessionCookieConfig() {
        return null;
    }

    @Override
    public void setSessionTrackingModes(Set<SessionTrackingMode> set) {

    }

    @Override
    public Set<SessionTrackingMode> getDefaultSessionTrackingModes() {
        return null;
    }

    @Override
    public Set<SessionTrackingMode> getEffectiveSessionTrackingModes() {
        return null;
    }

    @Override
    public void addListener(String s) {

    }

    @Override
    public <T extends EventListener> void addListener(T t) {

    }

    @Override
    public void addListener(Class<? extends EventListener> aClass) {

    }

    @Override
    public <T extends EventListener> T createListener(Class<T> aClass) throws ServletException {
        return null;
    }

    @Override
    public JspConfigDescriptor getJspConfigDescriptor() {
        return null;
    }

    @Override
    public ClassLoader getClassLoader() {
        return null;
    }

    @Override
    public void declareRoles(String... strings) {

    }

    @Override
    public String getVirtualServerName() {
        return null;
    }

    @Override
    public int getSessionTimeout() {
        return 0;
    }

    @Override
    public void setSessionTimeout(int i) {

    }

    @Override
    public String getRequestCharacterEncoding() {
        return null;
    }

    @Override
    public void setRequestCharacterEncoding(String s) {

    }

    @Override
    public String getResponseCharacterEncoding() {
        return null;
    }

    @Override
    public void setResponseCharacterEncoding(String s) {

    }
}
