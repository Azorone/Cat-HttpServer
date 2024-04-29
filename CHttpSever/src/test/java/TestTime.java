public class TestTime {

    public static void test(TestTask task) throws Exception{
        try {
            long start = System.currentTimeMillis();
            task.run();
            System.out.println("use time："+ (System.currentTimeMillis()-start)+"ms");
        }catch (Exception E){
            throw E;
        }


    }

}
