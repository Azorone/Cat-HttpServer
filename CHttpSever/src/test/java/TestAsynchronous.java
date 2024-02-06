import org.junit.Test;

import java.util.concurrent.LinkedBlockingQueue;

public class TestAsynchronous {
    @Test
    public void testJavaAsync(){


    }

    @Test
    public void testThreadInterrupted() throws InterruptedException {
        LinkedBlockingQueue<String> strings = new LinkedBlockingQueue<>();
      Thread t =  new Thread(new Runnable() {
            @Override
            public void run() {
                boolean i= true;
                while (i){
                    if (Thread.currentThread().isInterrupted()){

                        System.out.println(Thread.currentThread().getName()+ " 线程中断标志被设置");
                    }
                    try {
                        strings.take();
                    } catch (InterruptedException e) {
                        System.out.println("线程被中断");
                        i = false;
                        e.printStackTrace();
                    }
                }


            }
        },"t");
         t.start();
        Thread.sleep(1000);
        t.interrupt();
        System.out.println("开始中断线程T");
        t.join();
    }

    @Test
    public void testError(){

        int var = 0;
        int i = 1;
        while (i-- >-1){
            try {
                int vs = 1/i;
                var = 1;

            }catch (Exception e){}

            if (var == 0){
                System.out.println("45"+var);

            }
        }

    }
}
