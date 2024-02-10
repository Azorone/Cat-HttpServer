package org.cathttp.loaded;

import org.cathttp.base.net.inter.LifeCycle;
import org.cathttp.base.servlet.ServletProxy;
import org.cathttp.tools.FileFilter;
import org.cathttp.tools.StringTools;

import javax.servlet.Servlet;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class PackLoader implements Loader, LifeCycle {
    String basePath;
    String[] packPath;
    ArrayList<Class<?>> classArrayList = new ArrayList<>();
    ArrayList<HttpServlet> httpServlets = new ArrayList<>();
    Map<String, ServletProxy> httpServletMap = new HashMap<>();
    public String getPackPath(String packName){

        String packPath = StringTools.replaceFileSeparator(packName,".");
        String classPack = Objects.requireNonNull(PackLoader.class.getResource("/")).getPath();
        this.basePath = classPack;
        return classPack+packPath;
    }

    public Class<?> loadClass(String className) throws ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {

        return Class.forName(className);

    }

    public void walkClass(String name) throws ClassNotFoundException {
        ArrayDeque<File> fileDeque = new ArrayDeque<>();
        File file = new File(name);
        if (!file.exists()){
            return ;
        }
        fileDeque.add(file);
        while (!fileDeque.isEmpty()){

          File f=  fileDeque.remove();
          if (f.isDirectory()){
              File[] fs = f.listFiles();
              for (int i =0;i<fs.length;i++){
                  fileDeque.add(fs[i]);
              }
          }else {
             String path = f.getAbsolutePath();
             if (path.contains(".class")){
                 String s  = path.substring(basePath.length()-1,path.lastIndexOf(".class"));
                 Class<?>  c = Class.forName( s.replace("\\","."))  ;
                 if (c.isAssignableFrom(HttpServlet.class)){
                     Annotation[] annotations = c.getAnnotations();
                     for (int i=0;i<annotations.length;i++){
                         if (annotations[i] instanceof WebServlet){


                         }
                     }
                 }

             }
          }
        }

    }
    public void walkClass(FileFilter filter){

    }
    public PackLoader(){


    }

    @Override
    public void init() {
        try {
            walkClass(basePath);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void pause() {

    }
}
