package org.cathttp;

import org.cathttp.base.net.Accept;
import org.cathttp.global.GlobalConfig;
import org.cathttp.loaded.PackLoader;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Main {
    public static void main(String[] args) throws UnsupportedEncodingException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        System.out.println("Hello world!");


        //Accept a = new Accept(8080);
       Server server = new Server(8080);
        //PackLoader packLoader = new PackLoader("");
        //String path = packLoader.getPackPath("org.cathttp.tools");
        //System.out.println(path);
        //Class<?> c =  Class.forName("org.cathttp.tools.SysInfo");
        //Constructor<?> constructor =c.getConstructor();
        //Object o =  constructor.newInstance();
        //Method[] methods = c.getDeclaredMethods();
        //Method m = c.getDeclaredMethod("PrintSysInfoProperty",null);
       // m.invoke(o,null);
     //   PackLoader packLoader = new PackLoader();
       // packLoader.getPackPath("org.cathttp.base");
        //packLoader.init();
       // a.init();
        //a.start();
    }
}