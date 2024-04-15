import org.cathttp.tools.FileTool;
import org.junit.Test;

import java.io.File;

public class TestFileTool {
    @Test
   public void testFileToolGoDirFile(){
        File file = new File("C:/Users/Azero/Desktop/书籍");
        FileTool.GetDirFile(file,x->{
            System.out.println(x.getName());
        });
    }
    @Test
    public void testFileType(){
       System.out.println(FileTool.checkFileType(new File("static/test.html")));
    }
}
