import org.cathttp.global.Protocol;
import org.cathttp.http.request.HttpParser;
import org.cathttp.http.request.HttpParserResult;
import org.cathttp.http.respone.*;
import org.cathttp.javaee.context.ServletContextImp;
import org.cathttp.javaee.filter.FilterProxy;
import org.cathttp.http.RequestBuilder;
import org.cathttp.javaee.request.ServletRequestImplement;
import org.cathttp.javaee.response.ServletResponseImplements;
import org.cathttp.javaee.servlet.ServletProxy;
import org.cathttp.loaded.PackLoader;
import org.junit.Test;

import javax.servlet.http.Cookie;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Stack;

public class TestHttpParser {
    byte[] bytes = ("GET /test?asd[]=as&asd[]=bs HTTP/1.1\r\n" +
            "Host: localhost:8081\r\n" +
            "Connection: keep-alive\r\n" +
            "sec-ch-ua: \"Microsoft Edge\";v=\"119\", \"Chromium\";v=\"119\", \"Not?A_Brand\";v=\"24\"\r\n" +
            "sec-ch-ua-mobile: ?0\r\n" +
            "sec-ch-ua-platform: \"Windows\"\r\n" +
            "Upgrade-Insecure-Requests: 1\r\n" +
            "User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/119.0.0.0 Safari/537.36 Edg/119.0.0.0\r\n" +
            "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7\r\n" +
            "Sec-Fetch-Site: none\r\n" +
            "Sec-Fetch-Mode: navigate\r\n" +
            "Sec-Fetch-User: ?1\r\n" +
            "Sec-Fetch-Dest: document\r\n" +
            "Accept-Encoding: gzip, deflate, br\r\n" +
            "Accept-Language: zh-CN,zh;q=0.9,en;q=0.8,en-GB;q=0.7,en-US;q=0.6\r\n" +
            "Cookie: Webstorm-48a8547d=f204e5b1-ea4d-46fa-b93a-e6c1214e3374; Idea-797d35b7=81178a59-9499-4458-86ac-07b943c232d1; _xsrf=2|0f3de21e|4879811538151c976aec12b077739f9b|1694705174; username-localhost-8888=\"2|1:0|10:1694706919|23:username-localhost-8888|200:eyJ1c2VybmFtZSI6ICJjZjQzZjFjMjY3OGE0OThhYWJmZGE2MzI0NDY5ZGEyMiIsICJuYW1lIjogI" +
            "kFub255bW91cyBFdXJ5ZG9tZSIsICJkaXNwbGF5X25hbWUiOiAiQW5vbnltb3VzIEV1cnlkb21lIiwgImluaXRpYWxzIjogIkFFIiwgImNvbG9yIjogbnVsbH0=|26eee64b385b129b1e1ec379997d330431d7eb6911e2c5897cc35e547a6eb47b\"\r\n" +
            "\r\n").getBytes();
    @Test
    public void testHttpParser() throws Exception {
        System.out.println(bytes.length);
        HttpParser.httpParser(bytes);
    }
@Test
  public void testGetParameter() throws Exception {
    HttpParserResult result = HttpParser.httpParser(bytes);
    ServletRequestImplement requestImplement = RequestBuilder.builder(bytes);
    System.out.println( requestImplement.getRequestURI());
    System.out.println(requestImplement.getParameter("asd[]"));
    System.out.println(requestImplement.getParameter("asd"));
  }

    //测试上下文与解析器联合使用
    @Test
    public void testHttpRun() throws Exception {

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
            System.out.println(f.getUrlPatterns());
        }
        servletContextImp.setServletProxies(S);
        servletContextImp.setFilterProxies(F);
        servletContextImp.init0();
        servletContextImp.run(RequestBuilder.builder(bytes),new ServletResponseImplements());

    }
    @Test
    public void testBuildRes(){
        HttpResLine line = new HttpResLine(Protocol.HTTP1.getProtocol(), HttpResStatus.SUCCESS);
        HttpResHead resHead = new HttpResHead();
        resHead.putHead("User-Agent","asd");
        HttpResParserResult rp = new HttpResParserResult(line,resHead);
        byte[] bs = HttpResBuilder.buildHttpRes(rp);
        System.out.println(new String(bs, StandardCharsets.UTF_8));
    }
    @Test
    public void testCookieParse(){
        Cookie[] cookies;
        String c = "Webstorm-48a8547d=f204e5b1-ea4d-46fa-b93a-e6c1214e3374; a=b=v&s=a; Comment=545; Secure; Max-Age=154654; Idea-797d35b7=81178a59-9499-4458-86ac-07b943c232d1; _xsrf=2|0f3de21e|4879811538151c976aec12b077739f9b|1694705174";
        String[] cs = c.split("; ");
        Arrays.stream(cs).forEach(System.out::println);
        Stack<Cookie> cookieStack = new Stack<>();
        for (String s:cs){
            Cookie cookie = null;
            String[] p = s.split("=");
            String name;
            String v;
            int i = s.indexOf("=");
            if (i==-1&&s.equalsIgnoreCase("secure")){

                cookieStack.peek().setSecure(true);
                continue;
            }
            name = s.substring(0,i);
            v    = s.substring(i+1);



            if (name.equalsIgnoreCase("comment")){
                cookieStack.peek().setComment(v);
            }else if (name.equalsIgnoreCase("domain")){
                cookieStack.peek().setDomain(v);
            }else if (name.equalsIgnoreCase("max-age")){
                try {

                    cookieStack.peek().setMaxAge(Integer.parseInt(v));
                }catch (Exception e){
                    System.out.println("max-age 类型转换错误");
                }

            }else if (name.equalsIgnoreCase("path")){
                cookieStack.peek().setPath(v);

            }else if (name.equalsIgnoreCase("version")){
                cookieStack.peek().setPath(v);
            }else {
                cookie = new Cookie(name,v);
                cookieStack.add(cookie);
            }
        }
       int size = cookieStack.size();
        Cookie[] cos = new Cookie[size];
        cookieStack.toArray(cos);

    }

}
