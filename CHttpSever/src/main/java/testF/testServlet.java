package testF;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
@WebServlet(
        name = "testA",
        urlPatterns = "/testIO"
)
public class testServlet extends HttpServlet {
    Ees ees = new Ees();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        File file = new File("static/demo3");
        FileInputStream fileInputStream = new FileInputStream(file);
        int size = fileInputStream.available();
        byte[] bs = new byte[size];
        fileInputStream.read(bs);
        resp.getOutputStream().write(bs);
        resp.setContentType("text/plain; charset=utf-8");
        fileInputStream.close();

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("post 请求");
        resp.addCookie(new Cookie("name","jh"));
        req.getSession().setAttribute("name",req.getParameter("name"));
        Cookie[] cookies =  req.getCookies();
        System.out.println(req.getParameter("name"));
      //  super.doPost(req, resp);
    }
}
