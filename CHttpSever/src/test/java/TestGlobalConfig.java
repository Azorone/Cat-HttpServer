import org.cathttp.base.net.BusinessThreadPool;
import org.cathttp.global.GlobalConfig;
import org.junit.Test;

public class TestGlobalConfig {
    @Test
    public void testGlobal() throws InterruptedException {

        GlobalConfig globalConfig = GlobalConfig.getGlobalConfig();
        globalConfig.init();

        BusinessThreadPool businessThreadPool = GlobalConfig.globalConfig.businessThreadPool;
        businessThreadPool.submit(new Runnable() {
            @Override
            public void run() {
                System.out.println("yes");
            }
        });

    }
}
