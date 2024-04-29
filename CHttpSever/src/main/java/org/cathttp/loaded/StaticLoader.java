package org.cathttp.loaded;

import org.cathttp.tools.FileTool;
import org.cathttp.tools.StringTools;

import javax.swing.plaf.PanelUI;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class StaticLoader  {
    Map<String, File> fileMap = new HashMap<>();
    String real;
    String vir = "";
    String allow;

    public void setAllow(String allow) {
        this.allow = allow;
    }

    public void mapperStatic(String real, String vir){
        if (!vir.equals("/")){
            this.vir  = vir;
        }
        load(real);

    }
    void load(String dir){
        File file = new File(dir);
        this.real = file.getAbsolutePath();
        FileTool.GetDirFile(file,x->{
           String name = x.getName();
            if (StringTools.stringFileNameTailMatch(allow,name)){
               String key = x.getAbsolutePath().replace(file.getAbsolutePath(),vir);
                key = key.replace("\\","/");
               fileMap.put(key,x);
            }
        });
    }

    public void showPath(){
        for (Map.Entry<String,File> e: fileMap.entrySet()) {
            System.out.println(e.getKey()+": "+ e.getValue().getAbsolutePath());
        }
    }

    public File get(String virPath){
      return fileMap.get(virPath);
    }
    public void loadFileDisk(String virPath){
        String path;
        if (vir.isEmpty()){
            path = real + virPath;
        }else {
            path = virPath.replace(vir,real);
        }
        File file = new File(path);
        if (file.exists()){
            String key = file.getAbsolutePath().replace(file.getAbsolutePath(),vir);
           key = key.replace("\\","/");
            fileMap.put(key,file);
        }
    }
}
