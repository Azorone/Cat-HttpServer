import org.cathttp.javaee.servlet.ServletProxy;
import org.cathttp.loaded.PackLoader;
import org.cathttp.loaded.StaticLoader;
import org.junit.Test;
import testF.testServlet;

import javax.servlet.http.HttpServlet;
import java.io.File;
import java.util.Map;

import static org.cathttp.tools.SysInfo.PrintSysInfoProperty;

public class TestLoader {
    @Test
    public void testTools(){
        System.out.println(File.separatorChar);
        String PO = "com.java.cat";
        System.out.println(PO.replace(".",File.separator));
        PrintSysInfoProperty();
    }
    /**
     * 路径获取检查，
     * 当路径不存在的时候需要抛出异常，
     * 并停止加载，
     * **/
    @Test
    public void testClassPathLoader(){
        PackLoader packLoader = new PackLoader();
        String path = packLoader.getPackPath("testS");
        File file = new File(path);
        if (file.exists()){
           System.out.println(file.getPath());
        }else {
            System.out.println("不存在");
        }
    }
    /*****
     * HttpServlet 唯一性判断 不能出现重名
     * 一个路径映射多个Servlet实例
     *当出现重名，和一个路径映射多个实例时，服务器停止加载
     *
     * ***/

    @Test
    public void testStaticLoad(){
        StaticLoader s = new StaticLoader();
        s.mapperStatic("C:\\Users\\Azero\\Desktop\\LowCodeView","\\");
        s.showPath();
    }

}
