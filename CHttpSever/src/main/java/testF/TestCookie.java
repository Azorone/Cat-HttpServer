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
        name = "httpCookie",urlPatterns = "/addCookie"
)
public class TestCookie extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.addCookie(new Cookie("addCookie","test"));
        resp.getOutputStream().write("添加的Cookie是addCookie=test".getBytes(StandardCharsets.UTF_8));
        resp.setContentType("text/plain; charset=utf-8");
    }
}
