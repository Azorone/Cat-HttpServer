import org.cathttp.Task;
import org.cathttp.base.buffer.BuddyMalloc;
import org.cathttp.base.buffer.Message;
import org.junit.Test;

import java.io.*;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class TestBuffer {
    byte[] bytes = ("GET /test HTTP/1.1\r\n" +
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
            "kFub255bW91cyBFdXJ5ZG9tZSIsICJkaXNwbGF5X25hbWUiOiAiQW5vbnltb3VzIEV1cnlkb21lIiwgImluaXRpYWxzIjogIkFFIiwgImNvbG9yIjogbnVsbH0=|26eee64b385b129b1e1ec379997d330431d7eb6911e2c5897cc35e547a6eb47b\"\r\n\r\n").getBytes();
    @Test
    public void testRuntimeMallocBuffer() throws Exception {
        List<byte[]> bytes=  new ArrayList<>(1000000);
        TestTime.test(()->{
            byte[]    bs = new byte[3024*100000];
        });
        TestTime.test(()->{
            for (int i=0;i<100000;i++){
              byte[]  b = new byte[3024];
              bytes.add(b);
            }
        });
    }
    @Test
    public void StringByte() throws UnsupportedEncodingException {

    String s = "adasdasd是";
    System.out.println(s.length());
    System.out.println(s.getBytes(StandardCharsets.UTF_8).length);

    return;
    }

    @Test
    public void TestBuddyMallocIndex(){
        BuddyMalloc buddyMalloc = new BuddyMalloc(8,2048,true);
        buddyMalloc.init();
        buddyMalloc.allocate(257);
        buddyMalloc.showIndex();
    }
    @Test
    public void TestBuddyMallocAlloc() throws Exception {
        BuddyMalloc buddyMalloc = new BuddyMalloc(256,2048,true);
        buddyMalloc.init();
      //  buddyMalloc.allocate(30000);
        System.out.println("-----------");

        TestTime.test(new TestTask() {
            @Override
            public void run() throws UnsupportedEncodingException {
                for (int i=0;i<100;i++){
                  buddyMalloc.allocate(129);
                }
                buddyMalloc.allocate(32768);
            }
        });
        System.out.println("-----------");
        buddyMalloc.showIndex();
    }
    @Test
    public void TestMessage(){

        BuddyMalloc buddyMalloc = new BuddyMalloc(64,1024,true);
        buddyMalloc.init();
        String s = "asjkdhjkashdjkahdA啊大客户啊代价函数ADKJSHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHH健康大时代啊介绍的阿商家电jakshjk啊精神抖擞啊实践活啊搜2打卡四六级大师课了解到了；打开老实交代动";
        byte[] bs = s.getBytes(StandardCharsets.UTF_8);
        System.out.println(bs.length);
        Message message = buddyMalloc.allocate(bs.length);
        message.fill(bs);
        byte[] ba = message.getByte();
        System.out.println(new String(ba,StandardCharsets.UTF_8));
        byte[] bas = message.getByte();
        System.out.println(bas.length);
        System.out.println(new String(bas,StandardCharsets.UTF_8));
    }

    @Test
    public void testBuddyMallocBigAlloc() throws IOException {
        BuddyMalloc buddyMalloc = new BuddyMalloc(512,2048,true);
        buddyMalloc.init();
        File file = new File("static/TEST2.jpg");
        if (file.exists()){
            FileInputStream fileInputStream = new FileInputStream(file);
            byte[] bs = new byte[fileInputStream.available()];
            fileInputStream.read(bs);
           Message message = buddyMalloc.allocate(bs.length);
            message.fill(bs);
            buddyMalloc.showIndex();
        }

    }
}
