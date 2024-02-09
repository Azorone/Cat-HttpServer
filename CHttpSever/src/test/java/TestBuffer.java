import org.junit.Test;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;


public class TestBuffer {
@Test
    public void testBuffer() throws Exception {
        TestTime.test(()->{

            ByteBuffer buf  = ByteBuffer.allocateDirect(1024);

            String s =
                    "GET / HTTP/1.1\r\n" +
                    "Host: localhost:8080\n" +
                    "Connection: keep-alive\n" +
                    "sec-ch-ua: \"Not A(Brand\";v=\"99\", \"Google Chrome\";v=\"121\", \"Chromium\";v=\"121\"\n" +
                    "sec-ch-ua-mobile: ?0\n" +
                    "sec-ch-ua-platform: \"Windows\"\n" +
                    "Upgrade-Insecure-Requests: 1\n" +
                    "User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/121.0.0.0 Safari/537.36\n" +
                    "Sec-Purpose: prefetch;prerender\n" +
                    "Purpose: prefetch\n" +
                    "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7\n" +
                    "Sec-Fetch-Site: none\n" +
                    "Sec-Fetch-Mode: navigate\n" +
                    "Sec-Fetch-User: ?1\n" +
                    "Sec-Fetch-Dest: document\n" +
                    "Accept-Encoding: gzip, deflate, br\n" +
                    "Accept-Language: zh-CN,zh;q=0.9,am;q=0.8,en;q=0.7\n" +
                    "Cookie: 唉Idea-797d2e34=a872b58a-67f5-40be-9f4a-6e54351d8061; Pycharm-ffc04b0d=556c1a9a-9c3f-49ae-99f7-308cc2ef4583; Idea-797d35b7=81178a59-9499-4458-86ac-07b943c232d1; Idea-797d3977=74a4adcb-882f-4260-84fd-553bceb69286\n";

            byte[] bytes =  s.getBytes("UTF8");

            buf.put(bytes);
            buf.flip();
           int l = bytes.length;
           byte[] bs = new byte[l];
            buf.get(bs,0,l);
            System.out.println(new String(bs, StandardCharsets.UTF_8));

        });
}
}
