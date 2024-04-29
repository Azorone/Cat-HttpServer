package testF;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@WebServlet(
        name = "testBs",
        urlPatterns = "/test"
)
public class TestServlet02 extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String s  = "Session:name="+(String) req.getSession().getAttribute("name");
                resp.getOutputStream().write(s.getBytes(StandardCharsets.UTF_8));
        resp.setContentType("text/plain; charset=utf-8");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getSession().setAttribute("name",req.getParameter("name"));
        Cookie[] cookies = req.getCookies();
        String s = null ;
        if (cookies!=null){
            for (Cookie c:cookies){
                if (c.getName().equals("JSESSIONID")){
                    s= "你的JESSION = " +c.getValue();
                }
            }
            if (s!=null){
                resp.getOutputStream().write(s.getBytes(StandardCharsets.UTF_8));
            }
        }

        resp.setContentType("text/plain; charset=utf-8");
    }
}
