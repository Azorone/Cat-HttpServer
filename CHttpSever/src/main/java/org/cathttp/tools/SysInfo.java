package org.cathttp.tools;

import java.util.Map;
import java.util.Properties;
import java.util.Set;

import static java.lang.System.getenv;

public class SysInfo {

    public static void PrintAllSysInfoEnv(){
       Map<String,String> sys  =  System.getenv();

       for (Map.Entry<String,String> E :sys.entrySet()){
           System.out.println(E.getKey()+":"+E.getValue());
       }

    }

    public static void PrintSysInfoProperty(){

    //   System.out.println( System.getProperty("os.name"));
      Properties properties = System.getProperties();
      Set<Map.Entry<Object,Object>> map = properties.entrySet();
      map.forEach(x->{
          System.out.println(x.getKey()+":"+x.getValue());
      });
    }


}
