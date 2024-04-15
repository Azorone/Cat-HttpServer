package org.cathttp.http;

import org.cathttp.http.request.HttpParser;
import org.cathttp.http.request.HttpParserResult;
import org.cathttp.javaee.request.ServletRequestImplement;

public class RequestBuilder {


    public static ServletRequestImplement builder(byte[] bytes) throws Exception {
        if (bytes==null){
            System.out.println("空");
            return null;
        }
        if (bytes.length == 0){
            return null;
        }
        HttpParserResult httpParserResult = HttpParser.httpParser(bytes);
        return new ServletRequestImplement(httpParserResult);

   }


}
