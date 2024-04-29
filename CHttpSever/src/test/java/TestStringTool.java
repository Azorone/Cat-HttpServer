import org.cathttp.tools.StringTools;
import org.junit.Test;

public class TestStringTool {
    @Test
    public void testStringMatchTail(){

        System.out.println(StringTools.stringFileNameTailMatch("(html|js)","das.html"));
    }
}
