package org.cathttp.loaded;

import org.cathttp.base.net.inter.LifeCycle;
import org.cathttp.tools.StringTools;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

public class PackLoader implements Loader, LifeCycle {
    String basePath;
    String[] packPath;
    ArrayList<Class<?>> classArrayList = new ArrayList<>();
    public String getPackPath(String packName){

        String packPath = StringTools.replaceFileSeparator(packName,".");
        String classPack = Objects.requireNonNull(PackLoader.class.getResource("/")).getPath();
        this.basePath = classPack;
        return classPack+packPath;
    }

    public Class<?> loadClass(String className) throws ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {

        return Class.forName(className);

    }

    public void walkClass(String name){
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
                 System.out.println(s.replace("\\","."));
             }
          }
        }

    }
    public PackLoader(){


    }

    @Override
    public void init() {
            walkClass(basePath);
    }

    @Override
    public void pause() {

    }
}
