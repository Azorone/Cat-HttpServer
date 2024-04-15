package org.cathttp.tools;

import org.cathttp.exception.FileToolException;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayDeque;
import java.util.Queue;

public class FileTool {
    public static void  GetDirFile(File file, AfterOptFile optFile){
            if (!file.isDirectory()){
                throw new FileToolException("the file not is Dir");
            }
            Queue<File> filesQueue = new ArrayDeque<>();
            filesQueue.add(file);
            while (!filesQueue.isEmpty()){

                File temp = filesQueue.remove();
                if (temp.isDirectory()){
                    File[] files = temp.listFiles();
                    for (File f:files) {
                           filesQueue.add(f);
                    }
                    continue;
                }

                optFile.afterOptFile(temp);
            }
    }
    public static String checkFileType(File file){

            String name = file.getName();
            String type =name.substring(name.lastIndexOf(".")+1);
            return type;
    }
    public static FileType checkFileTypeMagic(File file){
        return null;
    }
}
