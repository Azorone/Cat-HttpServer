package org.cathttp.http.respone;

public class HttpResLine {
   private String version;
   private HttpResStatus httpResStatus;

    public void setVersion(String version) {
        this.version = version;
    }

    public void setHttpResStatus(HttpResStatus httpResStatus) {
        this.httpResStatus = httpResStatus;
    }

    public HttpResLine(){};
   public HttpResLine(String version,HttpResStatus status){
       this.httpResStatus = status;
       this.version = version;
   };

   public String toString(){
        char c = 32;
       return version+c+httpResStatus.code+"\r\n";


   }


}
