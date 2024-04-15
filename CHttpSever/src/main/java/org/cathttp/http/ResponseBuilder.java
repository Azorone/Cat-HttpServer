package org.cathttp.http;

import org.cathttp.global.Protocol;
import org.cathttp.http.respone.HttpResHead;
import org.cathttp.http.respone.HttpResLine;
import org.cathttp.http.respone.HttpResParserResult;
import org.cathttp.http.respone.HttpResStatus;
import org.cathttp.javaee.response.ServletOutStreamImp;
import org.cathttp.javaee.response.ServletResponseImplements;

public class ResponseBuilder {
    public static ServletResponseImplements builderRes(){
        HttpResLine line = new HttpResLine(Protocol.HTTP1.getProtocol(), HttpResStatus.SUCCESS);
        HttpResHead resHead = new HttpResHead();
        HttpResParserResult result = new HttpResParserResult();
        result.setLine(line);
        result.setHead(resHead);
        return  new ServletResponseImplements(result,new ServletOutStreamImp());
    }
}
