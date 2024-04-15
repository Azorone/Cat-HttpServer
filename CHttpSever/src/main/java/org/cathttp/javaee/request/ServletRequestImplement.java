package org.cathttp.javaee.request;

import org.cathttp.base.net.inter.LifeCycle;
import org.cathttp.http.request.HttpHeader;
import org.cathttp.http.request.HttpLine;
import org.cathttp.http.request.HttpParserResult;
import org.cathttp.javaee.context.ServletContextImp;
import org.cathttp.javaee.session.SessionManger;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.security.Principal;
import java.util.*;

public class ServletRequestImplement implements HttpServletRequest , LifeCycle {

    Map<String,Object> attributeMap =  new HashMap<>();
    Map<String,String[]> parameterMap = new HashMap<>();
    Cookie[] cookies ;
    HttpHeader httpHeader;
    HttpSession session;
    HttpLine httpLine;
    SessionManger sessionManger;
    ServletInputStream stream;
    String jsessionid;
    //byte[] inBuf;
    private String uri;

    @Override
    public void init() {

    }

    public void setSession(HttpSession session){
        this.session =session;
    }
    public void setSessionManger(SessionManger sessionManger){
        this.sessionManger = sessionManger;
    }
    @Override
    public void pause() {

    }

    ServletContext servletConfig  = ServletContextImp.getServletContext();
    private HttpParserResult httpParserResult;

    public ServletRequestImplement(HttpParserResult httpParserResult){
        Map<String,ArrayList<String>> mapTemp = new HashMap<>();
        this.httpParserResult = httpParserResult;
        this.httpHeader = httpParserResult.getHttpHeader();
        httpHeader.parseCookie();
        this.httpLine = httpParserResult.getHttpLine();
        httpParserResult.getHttpLine().getUrl();
        if (httpLine.getMeth().equalsIgnoreCase("post")){
            if (httpParserResult.getHttpHeader().get("Content-Type").equalsIgnoreCase("application/x-www-form-urlencoded")){
                byte[] bs = httpParserResult.getHttpBody().getBs();
                String s = new String(bs, StandardCharsets.UTF_8);
                String[] par= s.split("&");
                for (String p:par) {
                    String [] p0 = p.split("=");
                    if (p0.length>=2){
                        ArrayList<String> strings = mapTemp.get(p0[0]);
                        if (strings==null){
                            ArrayList<String> vs = new ArrayList<>();
                            String e = URLDecoder.decode(p0[1], StandardCharsets.UTF_8);
                            vs.add(e);
                            mapTemp.put(p0[0],vs);
                        }else {
                            String e = URLDecoder.decode(p0[1], StandardCharsets.UTF_8);
                            strings.add(e);
                        }
                    }
                }
            }
        }
       String s = httpLine.getQueryString();
        if (s==null){
            for (Map.Entry<String,ArrayList<String>> E:mapTemp.entrySet()){
                ArrayList<String> arrayList = E.getValue();
                String[] strings =new String[arrayList.size()] ;
                parameterMap.put(E.getKey(),arrayList.toArray(strings));
            }
            return;
        }
        String[] par= s.split("&");
       System.out.println(par.length);
        for (String p:par) {
            String [] p0 = p.split("=");
            if (p0.length>=2){
                ArrayList<String> strings = mapTemp.get(p0[0]);
                if (strings==null){
                    ArrayList<String> vs = new ArrayList<>();
                    vs.add(p0[1]);
                    mapTemp.put(p0[0],vs);
                }else {
                    strings.add(p0[1]);
                }
            }

        }
        for (Map.Entry<String,ArrayList<String>> E:mapTemp.entrySet()){
            ArrayList<String> arrayList = E.getValue();
            String[] strings =new String[arrayList.size()] ;
            parameterMap.put(E.getKey(),arrayList.toArray(strings));
        }
    }
    public ServletRequestImplement(HttpSession session,HttpHeader httpHeader,HttpLine line, Map<String,String[]> parameterMap,Cookie[] cookies){
        this.httpLine = line;
        this.session = session;
        this.httpHeader = httpHeader;
        this.parameterMap = parameterMap;
        this.cookies = cookies;
    }
   public ServletRequestImplement(String uri){
       this.uri = uri;
   }
    public ServletRequestImplement(){};
    @Override
    public Object getAttribute(String s) {

        return attributeMap.get(s);
    }

    @Override
    public Enumeration<String> getAttributeNames() {
        Set<String> set = attributeMap.keySet();
        Vector<String> stringVector = new Vector<>(set);

        return stringVector.elements();
    }

    @Override
    public String getCharacterEncoding() {
        return "utf-8";
    }

    @Override
    public void setCharacterEncoding(String s) throws UnsupportedEncodingException {

    }

    @Override
    public int getContentLength() {

         String v  = httpParserResult.getHttpHeader().get("Content-Length");
         if (v!=null){
             return Integer.parseInt(v);
         }
       return 0;
    }

    @Override
    public long getContentLengthLong() {
        String v  = httpParserResult.getHttpHeader().get("Content-Length");
        if (v!=null){
            return Long.parseLong(v);
        }
        return 0;
    }

    @Override
    public String getContentType() {
            return httpParserResult.getHttpHeader().get("Content-Type");
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        return stream;
    }

    @Override
    public String getParameter(String s) {

      if (parameterMap.get(s)!=null){
          return parameterMap.get(s)[0];
      }else {
          s = s+"[]";
          if (parameterMap.get(s)!=null){
              return parameterMap.get(s)[0];
          }
      }
        return "";
    }

    @Override
    public Enumeration<String> getParameterNames() {
        Set<String> set = parameterMap.keySet();
        Vector<String> nameVector = new Vector<>(set);
        return nameVector.elements();
    }

    @Override
    public String[] getParameterValues(String s) {
       if (parameterMap.get(s)==null){
           return parameterMap.get(s+"[]");
       }
        return parameterMap.get(s);
    }

    @Override
    public Map<String, String[]> getParameterMap() {
        return parameterMap;
    }
    /*****
     * 只支持http
     * *****/
    @Override
    public String getProtocol() {
        return httpParserResult.getHttpLine().getHttpVersion();
    }

    @Override
    public String getScheme() {
        return "http";
    }

    @Override
    public String getServerName() {
        return "(AYR)";
    }

    @Override
    public int getServerPort() {
        return 0;
    }

    @Override
    public BufferedReader getReader() throws IOException {
        return null;
    }

    @Override
    public String getRemoteAddr() {
        return null;
    }

    @Override
    public String getRemoteHost() {
        return null;
    }

    @Override
    public void setAttribute(String s, Object o) {
                this.attributeMap.put(s,o);
    }

    @Override
    public void removeAttribute(String s) {
                this.attributeMap.remove(s);
    }

    @Override
    public Locale getLocale() {
        return null;
    }

    @Override
    public Enumeration<Locale> getLocales() {
        return null;
    }

    @Override
    public boolean isSecure() {
        return false;
    }

    @Override
    public RequestDispatcher getRequestDispatcher(String s) {
        return servletConfig.getRequestDispatcher(s);
    }

    @Override
    public String getRealPath(String s) {
        return null;
    }

    @Override
    public int getRemotePort() {
        return 0;
    }

    @Override
    public String getLocalName() {
        return null;
    }

    @Override
    public String getLocalAddr() {
        return null;
    }

    @Override
    public int getLocalPort() {
        return 0;
    }

    @Override
    public ServletContext getServletContext() {
        return null;
    }

    @Override
    public AsyncContext startAsync() throws IllegalStateException {
        return null;
    }

    @Override
    public AsyncContext startAsync(ServletRequest servletRequest, ServletResponse servletResponse) throws IllegalStateException {
        return null;
    }

    @Override
    public boolean isAsyncStarted() {
        return false;
    }

    @Override
    public boolean isAsyncSupported() {
        return false;
    }

    @Override
    public AsyncContext getAsyncContext() {
        return null;
    }

    @Override
    public DispatcherType getDispatcherType() {
        return null;
    }

    @Override
    public String getAuthType() {
        return null;
    }

    @Override
    public Cookie[] getCookies() {

        return httpHeader.getCookies();
    }

    @Override
    public long getDateHeader(String s) {
        return 0;
    }

    @Override
    public String getHeader(String s) {
        return null;
    }

    @Override
    public Enumeration<String> getHeaders(String s) {
        return null;
    }

    @Override
    public Enumeration<String> getHeaderNames() {
        return null;
    }

    @Override
    public int getIntHeader(String s) {
        return 0;
    }

    @Override
    public String getMethod() {
        return httpLine.getMeth().toUpperCase();
    }

    @Override
    public String getPathInfo() {
        return null;
    }

    @Override
    public String getPathTranslated() {
        return null;
    }

    @Override
    public String getContextPath() {
        return null;
    }

    @Override
    public String getQueryString() {
        return null;
    }

    @Override
    public String getRemoteUser() {
        return null;
    }

    @Override
    public boolean isUserInRole(String s) {
        return false;
    }

    @Override
    public Principal getUserPrincipal() {
        return null;
    }

    @Override
    public String getRequestedSessionId() {
        return null;
    }

    @Override
    public String getRequestURI() {
        String URItemp =  httpParserResult.getHttpLine().getUrl();
        int index = URItemp.indexOf('?');
        if (index==-1){
           return URItemp;
        }

        return URItemp.substring(0,index);
    }

    @Override
    public StringBuffer getRequestURL() {
        return null;
    }

    @Override
    public String getServletPath() {
        return null;
    }

    @Override
    public HttpSession getSession(boolean b) {
        return null;
    }

    @Override
    public HttpSession getSession() {
        return session;
    }

    @Override
    public String changeSessionId() {
        return null;
    }

    @Override
    public boolean isRequestedSessionIdValid() {
        return false;
    }

    @Override
    public boolean isRequestedSessionIdFromCookie() {
        return false;
    }

    @Override
    public boolean isRequestedSessionIdFromURL() {
        return false;
    }

    @Override
    public boolean isRequestedSessionIdFromUrl() {
        return false;
    }

    @Override
    public boolean authenticate(HttpServletResponse httpServletResponse) throws IOException, ServletException {
        return false;
    }

    @Override
    public void login(String s, String s1) throws ServletException {

    }

    @Override
    public void logout() throws ServletException {

    }

    @Override
    public Collection<Part> getParts() throws IOException, ServletException {
        return null;
    }

    @Override
    public Part getPart(String s) throws IOException, ServletException {
        return null;
    }

    @Override
    public <T extends HttpUpgradeHandler> T upgrade(Class<T> aClass) throws IOException, ServletException {
        return null;
    }
}
