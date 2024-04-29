package org.cathttp.http.request;

import org.cathttp.http.HttpBody;

import javax.servlet.http.Cookie;

public class HttpParserResult {

    HttpLine httpLine;
    HttpHeader httpHeader;
    HttpBody httpBody;

    Cookie[] cookies;
    public HttpLine getHttpLine() {
        return httpLine;
    }

    public HttpParserResult setHttpLine(HttpLine httpLine) {
        this.httpLine = httpLine;
        return this;
    }

    public HttpHeader getHttpHeader() {
        return httpHeader;
    }

    public HttpParserResult setHttpHeader(HttpHeader httpHeader) {
        this.httpHeader = httpHeader;
        return this;
    }

    public HttpBody getHttpBody() {
        return httpBody;
    }

    public HttpParserResult setHttpBody(HttpBody httpBody) {
        this.httpBody = httpBody;
        return this;
    }
}
