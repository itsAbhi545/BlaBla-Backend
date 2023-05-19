package com.example.BlaBlaBackend.util;

public class Helper {
    public static String findExtension(String path){
        for(int i=path.length()-1;i>=0;i--){
            if(path.charAt(i)=='.') return path.substring(i);
        }
        //control never reach here
        return ".";
    }
}
