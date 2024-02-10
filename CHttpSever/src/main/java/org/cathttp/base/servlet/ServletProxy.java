package org.cathttp.base.servlet;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Set;

public class ServletProxy {
    HttpServlet servlet;
    Set<String> urlPatterns;
    String name;

    public void doService(ServletRequestImplement request, ServletResponse response) throws ServletException, IOException {

        servlet.service(request,response);

    }

}
