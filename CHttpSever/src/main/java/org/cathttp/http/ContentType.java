package org.cathttp.http;

public enum ContentType {
     HTML("text/html; charset=utf-8")
    ,PLAIN("text/plain; charset=utf-8")
    ,JAVASCRIPT("application/javascript; charset=utf-8")
    ,CSS("text/css; charset=utf-8")
    ,PNG("image/png")
    ,GIF("image/gif")
    ,JSON("application/json")
    ,WEBP("image/webp")
    ,JPG("image/jpg")
    ,XML("text/html; charset=utf-8")

    ;
   public String value;

    ContentType(String type){
        this.value= type;
    }
}
