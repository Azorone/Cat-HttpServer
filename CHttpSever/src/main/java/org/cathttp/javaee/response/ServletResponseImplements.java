package org.cathttp.javaee.response;

import org.cathttp.base.buffer.Message;
import org.cathttp.http.respone.HttpResHead;
import org.cathttp.http.respone.HttpResParserResult;
import org.cathttp.http.respone.HttpResStatus;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Locale;

public class ServletResponseImplements implements HttpServletResponse {
    HttpResParserResult result;
    ServletOutputStream stream;
    Message message = null;

    byte[] write =null;

    public byte[] getWrite() {
        return write;
    }

    public void setWrite(byte[] write) {
        this.write = write;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public ServletResponseImplements(HttpResParserResult result, ServletOutputStream stream){
        this.result = result;
        this.stream = stream;
    }
    public ServletResponseImplements(){};

    @Override
    public void addCookie(Cookie cookie) {
        HttpResHead head = this.result.getHead();
        if (head!=null){
            StringBuilder cv = new StringBuilder();
            cv.append(cookie.getName()).append("=").append(cookie.getValue());
            if (cookie.getDomain()!=null){
                cv.append("; ").append("Domain=").append(cookie.getDomain());
            }
            if (cookie.getMaxAge()!=-1){
                cv.append("; ").append("Max-Age=").append(cookie.getMaxAge());
            }
            if (cookie.getPath()!=null){
                cv.append("; ").append("Path=").append(cookie.getPath());
            }
            if (cookie.getSecure()){
                cv.append("; ").append("Secure");
            }
            if (cookie.isHttpOnly()){
                cv.append("; ").append("HttpOnly");
            }

            head.putHead("Set-Cookie",cv.toString());
        }
    }
    @Override
    public boolean containsHeader(String s) {

        return result.getHead().getHeadMap().containsKey(s);
    }

    public HttpResParserResult getResult() {
        return result;
    }

    @Override
    public String encodeURL(String s) {
        //把session id 放上去
        return null;
    }

    @Override
    public String encodeRedirectURL(String s) {
        return null;
    }

    @Override
    public String encodeUrl(String s) {
        return null;
    }

    @Override
    public String encodeRedirectUrl(String s) {
        return null;
    }

    @Override
    public void sendError(int i, String s) throws IOException {

    }

    @Override
    public void sendError(int i) throws IOException {

    }

    @Override
    public void sendRedirect(String s) throws IOException {

    }

    @Override
    public void setDateHeader(String s, long l) {

    }

    @Override
    public void addDateHeader(String s, long l) {

    }

    @Override
    public void setHeader(String s, String s1) {
            this.result.getHead().putHead(s,s1);
    }

    @Override
    public void addHeader(String s, String s1) {
            this.result.getHead().putHead(s,s1);
    }

    @Override
    public void setIntHeader(String s, int i) {

    }

    @Override
    public void addIntHeader(String s, int i) {

    }

    @Override
    public void setStatus(int i) {
                switch (i){
                    case 200:result.getLine().setHttpResStatus(HttpResStatus.SUCCESS);
                    case 404:result.getLine().setHttpResStatus(HttpResStatus.NOT_FOUND);
                }
    }

    @Override
    public void setStatus(int i, String s) {

    }

    @Override
    public int getStatus() {
        return 0;
    }

    @Override
    public String getHeader(String s) {
        return result.getHead().getHead(s);
    }

    @Override
    public Collection<String> getHeaders(String s) {
        return null;
    }

    @Override
    public Collection<String> getHeaderNames() {

        return null;
    }

    @Override
    public String getCharacterEncoding() {
        return null;
    }

    @Override
    public String getContentType() {
        return null;
    }

    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        return this.stream;
    }

    @Override
    public PrintWriter getWriter() throws IOException {
        return null;
    }

    @Override
    public void setCharacterEncoding(String s) {

    }

    @Override
    public void setContentLength(int i) {
            result.getHead().putHead("Content-Length",String.valueOf(i));
    }

    @Override
    public void setContentLengthLong(long l) {

    }

    @Override
    public void setContentType(String s) {
            result.getHead().putHead("Content-Type",s);
    }

    @Override
    public void setBufferSize(int i) {

    }

    @Override
    public int getBufferSize() {
        return 0;
    }

    @Override
    public void flushBuffer() throws IOException {

    }

    @Override
    public void resetBuffer() {

    }

    @Override
    public boolean isCommitted() {
        return false;
    }

    @Override
    public void reset() {

    }

    @Override
    public void setLocale(Locale locale) {

    }

    @Override
    public Locale getLocale() {
        return null;
    }
}
