package org.cathttp.http.request;

import javax.servlet.http.Cookie;
import java.util.*;

public class HttpHeader {
    Map<String,String> heads = new HashMap<>(32);
    Cookie[] cookies ;
    public void put(String key,String value){
        if (key.equals("Cookie")){

        }
        heads.put( key.toLowerCase(), value);
    }
    public String get(String key){

        key = key.toLowerCase();
        if (key.equalsIgnoreCase("cookie")){
            parseCookie();
        }
        return heads.get(key);
    }

    public Map getMap(){
        return heads;
    }
    public Set<String> getAllHead(){
        return heads.keySet();

    }

    public void parseCookie(){

        String c = heads.get("cookie");
        if (c==null){
            this.cookies = null;
            return;}
        String[] cs = c.split("; ");
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
        this.cookies =   cookieStack.toArray(cos);
    }

    public Cookie[] getCookies() {
        return cookies;
    }
}
