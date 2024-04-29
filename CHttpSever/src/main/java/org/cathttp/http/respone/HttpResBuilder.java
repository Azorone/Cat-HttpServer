package org.cathttp.http.respone;

import java.nio.charset.StandardCharsets;

public class HttpResBuilder {
  public  static byte[] buildHttpRes(HttpResParserResult result){

        StringBuilder builder = new StringBuilder();
        return builder
                .append(result.getLine().toString())
                .append(result.getHead().toString())
                .toString().getBytes(StandardCharsets.UTF_8);


    }



}
