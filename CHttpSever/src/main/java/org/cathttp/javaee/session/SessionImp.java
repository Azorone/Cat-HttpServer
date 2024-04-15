package org.cathttp.javaee.session;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;
import java.io.Serial;
import java.io.Serializable;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public class SessionImp implements HttpSession, Serializable {

    @Serial
    private static final long serialVersionUID = 20242261016L;
    Map<String,Object> map = new HashMap<>();
    String JSESSIONID;
    ServletContext servletContext;
    @Override
    public long getCreationTime() {
        return 0;
    }

    @Override
    public String getId() {
        return null;
    }

    @Override
    public long getLastAccessedTime() {
        return 0;
    }

    @Override
    public ServletContext getServletContext() {
        return null;
    }

    @Override
    public void setMaxInactiveInterval(int i) {

    }

    @Override
    public int getMaxInactiveInterval() {
        return 0;
    }
    @Deprecated
    @Override
    public HttpSessionContext getSessionContext() {
        return null;
    }

    @Override
    public Object getAttribute(String s) {
        return map.get(s);
    }
    @Deprecated
    @Override
    public Object getValue(String s) {
        return map.get(s);
    }

    @Override
    public Enumeration<String> getAttributeNames() {
        return null;
    }

    @Override
    public String[] getValueNames() {
        return new String[0];
    }

    @Override
    public void setAttribute(String s, Object o) {
        map.put(s,o);
    }
    @Deprecated
    @Override
    public void putValue(String s, Object o) {

    }

    @Override
    public void removeAttribute(String s) {

    }
    @Deprecated
    @Override
    public void removeValue(String s) {

    }

    @Override
    public void invalidate() {

    }

    @Override
    public boolean isNew() {
        return false;
    }

    @Override
    public String toString() {
        return null;
    }
}
