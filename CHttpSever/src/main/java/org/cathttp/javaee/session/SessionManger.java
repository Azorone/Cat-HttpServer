package org.cathttp.javaee.session;

import org.cathttp.tools.IDBuilder;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SessionManger {
    private static final SessionManger InstanceSession= new SessionManger();
    private static final ConcurrentHashMap<String,SessionImp> sessionImpMap = new ConcurrentHashMap<>();
    public SessionImp creatSession(HttpServletResponse response){

        String sessionID = IDBuilder.builderId();
        SessionImp sessionImp = new SessionImp();
        Cookie cookie = new Cookie("JSESSIONID",sessionID);
        cookie.setMaxAge(60);
        response.addCookie(cookie);
        sessionImpMap.put(sessionID,sessionImp);
        return sessionImp;
    }

    public SessionImp getSession(String jsessionid){
        return sessionImpMap.get(jsessionid);
    }

    public  static SessionManger getInstanceSession() {
        return InstanceSession;
    }
}
