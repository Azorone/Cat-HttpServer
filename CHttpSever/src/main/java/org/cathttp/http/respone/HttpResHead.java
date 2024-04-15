package org.cathttp.http.respone;

import java.util.HashMap;
import java.util.Map;

public class HttpResHead {
   private Map<String,String> headMap = new HashMap<>();
   public HttpResHead(){
        headMap.put("Server","(AYR)");
        headMap.put("Connection","close");
    }
   public void putHead(String key,String value){

       headMap.put(key,value);


   }

   public Map<String,String> getHeadMap(){
       return headMap;
   }
   public String getHead(String key){
       return headMap.get(key);
   }

    @Override
    public String toString() {
       char c = 32;
        StringBuilder builder = new StringBuilder();
        for (Map.Entry<String,String> e: headMap.entrySet()){
                    builder.append(e.getKey()).append(":").append(c).append(e.getValue()).append("\r\n");
        }
        builder.append("\r\n");
        return builder.toString();
    }
}
