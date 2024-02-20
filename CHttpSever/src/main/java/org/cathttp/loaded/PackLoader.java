package org.cathttp.loaded;

import org.cathttp.base.net.inter.LifeCycle;
import org.cathttp.javaee.filter.FilterProxy;
import org.cathttp.javaee.servlet.ServletProxy;
import org.cathttp.tools.FileFilter;
import org.cathttp.tools.StringTools;

import javax.servlet.Filter;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebFilter;
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
    public ArrayList<Class<?>> classArrayList = new ArrayList<>();
    public ArrayList<ServletProxy> httpServlets = new ArrayList<>();
    public ArrayList<FilterProxy> filterProxies = new ArrayList<>();

    public String getPackPath(String packName){

        String packPath = StringTools.replaceFileSeparator(packName,".");
        String classPack = Objects.requireNonNull(PackLoader.class.getResource("/")).getPath();
        this.basePath = classPack;
        return classPack+packPath;
    }
    public Class<?> loadClass(String className) throws ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {

        return Class.forName(className);

    }
    public void walkClass(String name) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
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
                 if (Filter.class.isAssignableFrom(c)){
                     addFilter(c);
                 }else if (HttpServlet.class.isAssignableFrom(c)){
                     addServlet(c);
                 }
             }
          }
        }
    }
    void addServlet(Class<?> c) throws InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException {
            Annotation[] annotations = c.getAnnotations();
            for (int i=0;i<annotations.length;i++){
                if (annotations[i] instanceof WebServlet){

                    WebServlet webServlet = (WebServlet) annotations[i];
                    Constructor<HttpServlet> constructor = (Constructor<HttpServlet>) c.getConstructor();
                    HttpServlet object = constructor.newInstance();
                    ServletProxy proxy    = new ServletProxy(webServlet,object);
                    httpServlets.add(proxy);
                    break;
                }
            }
    }
    void addFilter(Class<?> c) throws InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException {

        Annotation[] annotations = c.getAnnotations();
        for (int i=0;i<annotations.length;i++){
            if (annotations[i] instanceof WebFilter){

                WebFilter webFilter = (WebFilter) annotations[i];
                Constructor<Filter> constructor = (Constructor<Filter>) c.getConstructor();
                Filter filter = constructor.newInstance();
                FilterProxy proxy    = new FilterProxy(webFilter,filter);
                filterProxies.add(proxy);
                break;
            }
        }
    }
    void addListener(Class<?> c){

    }
    public void walkClass(FileFilter filter){

    }
    public PackLoader(){

    }
    public PackLoader(String ...packs){
        this.packPath = new String[packs.length];

        for(int i=0;i< packs.length;i++){
            this.packPath[i] = getPackPath(packs[i]);
        }

    }
    @Override
    public void init() {
        try {
            if (packPath != null){
                for (String s:packPath) {
                    System.out.println(s);
                    walkClass(s);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public ArrayList<FilterProxy> getFilterProxies() {
        return filterProxies;
    }

    public ArrayList<ServletProxy> getHttpServlets() {
        return httpServlets;
    }

    @Override
    public void pause() {

    }
}
