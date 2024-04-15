package org.cathttp.http.respone;

public enum HttpResStatus {
    SUCCESS(200),
    NOT_FOUND(404);
    int code;
    HttpResStatus(int status){
        this.code =status;
    }

}
