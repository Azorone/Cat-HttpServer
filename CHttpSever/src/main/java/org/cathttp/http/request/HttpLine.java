package org.cathttp.http.request;

import java.util.Map;

public class HttpLine {
  private String HttpVersion;
  private String uri;
  private String meth;
  private String queryString;
  private String scheme;
  public String getHttpVersion() {
    return HttpVersion;
  }

  private void setQueryString(){
    if (uri!=null){
      String[] qs=  uri.split("\\?");

      if (qs.length>1){
        this.queryString = qs[1];
      }else {
        this.queryString = null;
      }


    }
  }
  public String getQueryString(){
    setQueryString();
    return queryString;
  }
  public void setHttpVersion(String httpVersion) {

    HttpVersion = httpVersion;
  }

  public String getUrl() {
    return uri;
  }

  public void setUrl(String uri) {
    this.uri = uri;
  }

  public String getMeth() {
    return meth;
  }

  public void setMeth(String meth) {
    this.meth = meth;
  }

  @Override
  public String toString() {
    return "HttpLine{" +
            "HttpVersion='" + HttpVersion + '\'' +
            ", url='" + uri + '\'' +
            ", meth='" + meth + '\'' +
            '}';
  }
}
