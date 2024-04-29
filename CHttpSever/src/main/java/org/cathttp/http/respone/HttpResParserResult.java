package org.cathttp.http.respone;

import org.cathttp.http.HttpBody;

public class HttpResParserResult {

   private HttpResHead head;
   private HttpResLine line;

    private  HttpBody    body;

    public HttpResHead getHead() {
        return head;
    }

    public void setHead(HttpResHead head) {
        this.head = head;
    }

    public HttpResLine getLine() {
        return line;
    }

    public void setLine(HttpResLine line) {
        this.line = line;
    }

    public HttpBody getBody() {
        return body;
    }

    public void setBody(HttpBody body) {
        this.body = body;
    }

    public HttpResParserResult(HttpResLine line, HttpResHead head ) {

        this.head = head;
        this.line = line;
    }

    public HttpResParserResult(HttpResHead head, HttpResLine line, HttpBody body) {
        this.head = head;
        this.line = line;
        this.body = body;

    }

    public HttpResParserResult() {
    }
}
