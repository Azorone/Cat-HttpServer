import org.cathttp.loaded.PackLoader;
import org.cathttp.tools.SysInfo;
import org.junit.Test;

import java.io.File;

import static org.cathttp.tools.SysInfo.PrintSysInfoProperty;

public class TestTool {
    @Test
    public void testTools(){
        System.out.println(File.separatorChar);
        String PO = "com.java.cat";
        System.out.println(PO.replace(".",File.separator));
        PrintSysInfoProperty();
    }
    @Test
    public void testClassPathLoader(){
        PackLoader packLoader = new PackLoader();
        String path = packLoader.getPackPath("tools");
        File file = new File(path);
        if (file.exists()){
           System.out.println(file.getPath());
        }else {
            System.out.println("不存在");
        }
    }
}
