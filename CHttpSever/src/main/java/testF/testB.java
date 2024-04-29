package testF;

import org.cathttp.base.net.Accept;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

@WebServlet(
        name = "testB",
        urlPatterns = "/cpuTest"
)
public class testB extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        double b = 3.1415;
        int i = 10;
        while(i-- > 0 ){
            for (int p=0;p<1000;p++){
                Random random = new Random();
               int x = random.nextInt(655537);
               b = x + b;
            }
            b = b+6.232265*b;
            b = Math.sqrt(b) /8.15312121;
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }
}
