package org.cathttp.http.request;


import org.cathttp.http.HttpBody;
import org.cathttp.http.httpexception.HttpParserException;

import javax.servlet.http.Cookie;

/***
 * 基于http的bnf范式的HTTP报文解析器
 * MultiValuedMap
 * http-message = start-line * (header-field CRLF)
 * CRLF
 * [message]
 * **/
public class HttpParser {
    private final static int ARRAY_SIZE = 128;
    private static final boolean[] IS_CONTROL = new boolean[ARRAY_SIZE];  //控制字符
    private static final boolean[] IS_DIGIT = new boolean[ARRAY_SIZE];    //数字
    private static final boolean[] IS_HEX  = new boolean[ARRAY_SIZE];     //16进制
    private static final boolean[] IS_CRLF = new boolean[ARRAY_SIZE];     //互联网标准换行
    private static final boolean[] IS_SP   = new boolean[ARRAY_SIZE];     //空格
    private static final boolean[] IS_HTTP_PROTOCOL = new boolean[128]; // HTTP
    private static final boolean[] IS_POST = new boolean[ARRAY_SIZE];  //POST方法
    private static final boolean[] IS_GET  = new boolean[ARRAY_SIZE]; //GET方法
    private static final boolean[] IS_SEPARATOR = new boolean[ARRAY_SIZE];
    private static final boolean[] IS_TOKEN     = new boolean[ARRAY_SIZE];
    private static final int MAX_SP = 60;
    static {
        for (int i=0;i<ARRAY_SIZE;i++){
            if (i<32 || i==127){

                IS_CONTROL[i] = true;
            }
            if (i>='0' && i<='9'){

                IS_DIGIT[i] = true;
            }
            if (    i == '(' || i == ')' || i == '<' || i == '>'  || i == '@'  ||
                    i == ',' || i == ';' || i == ':' || i == '\\' || i == '\"' ||
                    i == '/' || i == '[' || i == ']' || i == '?'  || i == '='  ||
                    i == '{' || i == '}' || i == ' ' || i == '\t') {
                IS_SEPARATOR[i] = true;
            }
            if (!IS_CONTROL[i] && !IS_SEPARATOR[i]) {

                IS_TOKEN[i] = true;
            }
            if (i == 'H' || i == 'T' || i == 'P' || i == '/' || i == '.' || (i >= '0' && i <= '9')) {
                IS_HTTP_PROTOCOL[i] = true;
            }
            if (i == 10 || i==13){
                IS_CRLF[i] = true;
            }
            if (i=='G'||i== 'E' || i == 'T'){
                IS_GET[i] = true;
            }
            if (i== 'P'||i=='O'|| i=='S' || i == 'T'){
                IS_POST[i] = true;
            }
            if (i==' '){
                IS_SP[i] = true;
            }
        }
    }
  static  public HttpParserResult httpParser(byte[] bytes) throws Exception {
            int start = -1;
            HttpLine httpLine = new HttpLine();
            HttpHeader httpHeader = new HttpHeader();
            HttpBody httpBody = new HttpBody();
            start = lineParser(bytes,start,httpLine);
            start = headerParser(bytes,start,httpHeader);
            if (httpLine.getMeth().equals("POST")){

               String value = httpHeader.get("Content-Length");
               if (value!=null){
                   messageParser(bytes,start,httpBody,Integer.parseInt(value));
               }
            }
            HttpParserResult httpParserResult = new HttpParserResult();

            httpParserResult.setHttpBody(httpBody)
                            .setHttpLine(httpLine)
                            .setHttpHeader(httpHeader);
            return httpParserResult;
    }
    public static int headerParser(byte[] bytes, int start, HttpHeader httpHeader) throws HttpParserException {
        start = getFiled(bytes,start,httpHeader);
        return start;
    }
    public static int getFiled(byte[] bytes, int start, HttpHeader httpHeader) throws HttpParserException {
        while (!isCRLF(bytes[start],bytes[start+1])){
         start = getRow(bytes,start,httpHeader);
        }
        start = start + 2;
        return start;
    }
    public static int getRow(byte[] bytes, int start, HttpHeader httpHeader) throws HttpParserException {
        StringBuilder key = new StringBuilder(64);
        StringBuilder value = new StringBuilder(64);

        while (IS_TOKEN[bytes[start]]){
            key.append((char) bytes[start++]);
        }

        if (bytes[start]==':')
        {start++;}
        else {
            throw new HttpParserException("head 分隔符格式错误");
        }
        int sp = 0;
        while (bytes[start]==' '){
            sp ++ ;
            start++ ;
            if (sp==MAX_SP){ //最大的空格
                throw new HttpParserException("非法的格式协议");
            }
        } //丢弃：后面的空格

        while (!isCRLF(bytes[start],bytes[start+1])){
            value.append((char) bytes[start++]);
        }
        if (value.length()==0){
            throw new HttpParserException("value 缺失");
        }
        if (isCRLF(bytes[start],bytes[start+1])){
            start += 2;
        }else {throw new HttpParserException("非法的格式,没 CRLF");};
        httpHeader.put(key.toString(), value.toString());
        return start;
    }
    public static int lineParser(byte[] bytes, int start, HttpLine httpLine) throws HttpParserException {
        int point = start+1;
        start = getMeth(bytes,point,httpLine);

        if (!IS_SP[bytes[start]])
        { throw new HttpParserException(" 分隔符应为空格" );  }

        start = getUrl(bytes,start+1,httpLine);

        if (!IS_SP[bytes[start]])
        { throw new HttpParserException(" 分隔符应为空格" );  }

        start = getHttpVersion(bytes,start+1,httpLine);

        if (!isCRLF(bytes[start],bytes[start+1])){
            throw new HttpParserException("行结束符异常");
        }
        start = start + 2;
        return start;
    }
    public static int messageParser(byte[] bytes, int start, HttpBody httpBody,int len){
        byte[] bs = new byte[len];
        System.arraycopy(bytes,start,bs,0,len);
        httpBody.setBs(bs);
        return start+len;
    }
    public static int getHttpVersion(byte[] bytes, int start, HttpLine httpLine) throws HttpParserException {
            if (bytes[start]=='H'&&bytes[start+1]=='T'&&bytes[start+2]=='T'&&bytes[start+3]=='P'&&bytes[start+4]=='/'){
                StringBuilder stringBuilder = new StringBuilder(64);
                stringBuilder.append("HTTP/");
                start = start + 5;

                if (IS_DIGIT[ bytes[start]]){
                    stringBuilder.append((char) bytes[start]);
                    start ++ ;
                }else {
                        throw  new HttpParserException("NOT IS HTTP ");
                }

                while (IS_DIGIT[bytes[start]]){

                    stringBuilder.append((char) bytes[start]);
                    start ++ ;
                }

                if (bytes[start]!='.'){
                        throw new HttpParserException("NOT IS HTTP");
                }else {
                    stringBuilder.append((char) bytes[start]);
                    start++;}

                while (IS_DIGIT[bytes[start]]){

                    stringBuilder.append((char) bytes[start]);
                    start ++ ;
                }

                httpLine.setHttpVersion(stringBuilder.toString());
            }

                return start;

    }
    public static int getUrl(byte[] bytes, int start, HttpLine httpLine){ //url的标准
        StringBuilder builder = new StringBuilder(60);

        while (!IS_SP[bytes[start]]){
            builder.append((char) bytes[start++]);
        }
        httpLine.setUrl(builder.toString());
        return start;
    }
    public static int getMeth(byte[] bytes, int start, HttpLine httpLine){

        if (isGET(bytes, start)){

            httpLine.setMeth(RequestMethod.GET.name());
            return start + 3;

        }else if (isPost(bytes,start)){

            httpLine.setMeth(RequestMethod.POST.name());
            return start + 4;

        }else if (isOPTIONS(bytes,start)){
            httpLine.setMeth(RequestMethod.OPTIONS.name());
            return start + 7;
        }else if (isDELETE(bytes,start)){
            httpLine.setMeth(RequestMethod.DELETE.name());
            return start + 6;
        }else if (isTRACE(bytes,start)){
            httpLine.setMeth(RequestMethod.TRACE.name());
            return start + 5;
        }else if (isCONNECT(bytes,start)){
            httpLine.setMeth(RequestMethod.CONNECT.name());
            return start + 7;
        }else if (isHEAD(bytes,start)){
            httpLine.setMeth(RequestMethod.HEAD.name());
            return start+ 4;
        }else if (isPUT(bytes,start)){
            httpLine.setMeth(RequestMethod.PUT.name());
            return start+3;
        }

        return -1;
    }
    public static boolean isCRLF(int c, int c2){

        return c==13 && c2==10;
    }
    public boolean isHttp(int c){

        return IS_HTTP_PROTOCOL[c];

    }
    public static boolean isGET(byte[] bytes, int start){

        return        bytes[start]  == 'G'
                   && bytes[start+1]== 'E'
                   && bytes[start+2]== 'T';
    }
    public static boolean isPost(byte[] bytes, int start){

        return          bytes[start] == 'P'
                   && bytes[start+1] == 'O'
                   && bytes[start+2] == 'S'
                   && bytes[start+3] == 'T';

    }
    public static boolean isHEAD(byte[] bytes, int start){
        return     bytes[start] == 'H'
                && bytes[start+1] == 'E'
                && bytes[start+2] == 'A'
                && bytes[start+3] == 'D';

    }
    public static boolean isPUT(byte[] bytes, int start){
        return       bytes[start] == 'P'
                && bytes[start+1] == 'U'
                && bytes[start+2] == 'T';

    };
    public static boolean isDELETE(byte[] bytes, int start){
        return      bytes[start] == 'D'
                && bytes[start+1] == 'E'
                && bytes[start+2] == 'L'
                && bytes[start+3] == 'E'
                && bytes[start+4] == 'T'
                && bytes[start+5] == 'E';
    };
    public static boolean isCONNECT(byte[] bytes, int start){
        return       bytes[start] == 'C'
                && bytes[start+1] == 'O'
                && bytes[start+2] == 'N'
                && bytes[start+3] == 'N'
                && bytes[start+4] == 'E'
                && bytes[start+5] == 'C'
                && bytes[start+6] == 'T';
    }
    public static boolean isOPTIONS(byte[] bytes, int start){
        return      bytes[start] == 'O'
                && bytes[start+1] == 'P'
                && bytes[start+2] == 'T'
                && bytes[start+3] == 'I'
                && bytes[start+4] == 'O'
                && bytes[start+5] == 'N'
                && bytes[start+6] == 'S';
    }
    public static boolean isTRACE(byte[] bytes, int start){
        return      bytes[start] == 'T'
                && bytes[start+1] == 'R'
                && bytes[start+2] == 'A'
                && bytes[start+3] == 'C'
                && bytes[start+4] == 'E';
    }

    public static int getCookieSize(){


        return 0;
    }
    public static Cookie[] parseCookie(String cookie){

     return null;

    }
}
