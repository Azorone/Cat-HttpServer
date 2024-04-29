package org.cathttp.base.app;

import org.cathttp.Server;
import org.cathttp.base.net.inter.LifeCycle;
import org.cathttp.http.ContentType;
import org.cathttp.loaded.StaticLoader;
import org.cathttp.tools.FileTool;
import org.cathttp.tools.FileType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;

public class StaticAppWeb  implements LifeCycle {

    public static StaticAppWeb staticAppWeb = new StaticAppWeb(Server.staticDir,Server.vir,Server.allow);
    public static StaticAppWeb getStaticAppWeb() {
        return staticAppWeb;
    }
    private  StaticLoader staticLoader = new StaticLoader();
    private String dir;
    private   String vir;
    private   String allows;
    private StaticAppWeb(String Realdir, String vir, String allows){

        this.allows = allows;
        this.dir = Realdir;
        this.vir = vir;
    //    staticLoader.setAllow(allows);
      //  staticLoader.mapperStatic(dir,vir);
    }

   public boolean getStaticFile(HttpServletRequest request, HttpServletResponse response){
        File file = staticLoader.get(request.getRequestURI());
        if (file == null){
            return false;
        }
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            byte[] bs = new byte[fileInputStream.available()] ;
            fileInputStream.read(bs);
            response.getOutputStream().write(bs);
            setResponseContentType(FileTool.checkFileType(file),response, bs.length);
            //response.setContentType("text/html; charset=utf-8");
            fileInputStream.close();
        }catch (Exception io){
            System.out.println("静态资源加载错误");

        }
        return true;
    }
    public void  setResponseContentType(String type,HttpServletResponse response,int length){
        if (type.equals(FileType.html.name())){

            response.setContentType(ContentType.HTML.value);
        }else if (type.equals(FileType.js.name())){

            response.setContentType(ContentType.JAVASCRIPT.value);
        }else if (type.equals(FileType.css.name())){

            response.setContentType(ContentType.CSS.value);
        }else if (type.equals(FileType.png.name())){

            response.setContentType(ContentType.PNG.value);
        }else if (type.equals(FileType.webp.name())){

            response.setContentType(ContentType.WEBP.value);
        }else if (type.equals(FileType.json.name())){
            response.setContentType(ContentType.JSON.value);
        }else if (type.equals(FileType.jpg.name())){
            response.setContentType(ContentType.JPG.value);
        }else if (type.equals(FileType.xml.name())){
            response.setContentType(ContentType.XML.value);
        }
          //  response.setContentLength(length);
    }

    @Override
    public void init() {
        System.out.println("静态文件加载器开始初始化中....");
        staticLoader.setAllow(allows);
        staticLoader.mapperStatic(dir,vir);
    }

    @Override
    public void pause() {
        staticLoader = null;

    }

}
