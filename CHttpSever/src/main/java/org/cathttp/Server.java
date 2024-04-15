package org.cathttp;

import org.cathttp.base.app.ServletAppWeb;
import org.cathttp.base.app.StaticAppWeb;
import org.cathttp.base.net.Accept;
import org.cathttp.base.net.inter.LifeCycle;
import org.cathttp.global.GlobalConfig;

public class Server implements LifeCycle {
    Accept accept;
    public static String staticDir = "static";
    public static String vir = "/";
    public static String[] packs;
    public static int port = 8080;
    public static String allow= "(js|html|png|webp|jpg|css)";
    public static String root= "/";
    public Server(String...pack){
        Server.packs = pack;
      //  init();
    }
    @Override
    public void init() {
        System.out.println("Server 开始初始化...");
        ServletAppWeb.getInstance(Server.packs).init(); //初始化容器
        StaticAppWeb.getStaticAppWeb().init();  //初始化静态资源加载器
        GlobalConfig config = GlobalConfig.getGlobalConfig();
        config.init();
        accept = new Accept(Server.port);
        accept.init();
        accept.start();

    }
    public void run(){

        init();
        System.out.println("Server初始化完成 协议：HTTP 端口："+port);
    }
    public Server configStaticDir(String real,String vir){
        Server.staticDir = real;
        Server.vir = vir;
        return this;
    }
    public Server configPort(int port){
        Server.port = port;
        return this;
    }
    public Server configServerName(String name){
        return this;
    }
    @Override
    public void pause() {

    }
}
