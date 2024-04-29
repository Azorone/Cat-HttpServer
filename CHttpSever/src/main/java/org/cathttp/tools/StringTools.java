package org.cathttp.tools;

import javax.swing.*;
import java.io.File;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class StringTools {


   public final static Charset  UTF8 = StandardCharsets.UTF_8;
   public final static Charset  ISO  = StandardCharsets.ISO_8859_1;
    public static String  replace(String src,String tar,String re){
           return src.replace(tar,re);
    }
    public static String replaceFileSeparator(String src,String tar){
    return src.replace(tar,"/");
    }
    public static String byteToString(byte[] bs, Charset charset){

        return new String(bs, charset);
    }
    public static byte[] stringToByte(String str,Charset charset){
        return str.getBytes(charset);
    }
    public static byte[] stringToByte(StringBuilder str,Charset charset){
        return str.toString().getBytes(charset);
    }

    public static boolean stringFileNameTailMatch(String exp,String src){
        String regex = ".*\\."+exp+"$";
        return src.matches(regex);
    }

}
