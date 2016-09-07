package com.base.modules.util;

import org.apache.commons.io.FileUtils;

import java.io.File;

/**
 * Created by micheal on 15/7/8.
 */
public class FileUtilsEx extends FileUtils{
    public static void createFold(String path){
        File root = new File(path);
        if(!root.exists()){
            root.mkdirs();
        }
    }

    /**
     * @param base
     * @param paths
     * @return
     */
    public static String joinPaths(String base, String... paths) {

        StringBuilder buf = new StringBuilder().append(base);

        for (String path : paths) {
            buf.append(File.separator).append(path);
        }

        return PathFormater.formate(buf.toString());
    }

    /**
     * @param fileName
     * @return
     */
    public static String getFileNameWithoutExtension(String fileName) {
        if (fileName.indexOf(".") != -1) {
            return fileName.substring(0, fileName.lastIndexOf("."));
        }
        return fileName;
    }

    /**
     * @param fileName
     * @return
     */
    public static String getFileExtensionWithDot(String fileName) {
        if (fileName.indexOf(".") != -1) {
            return fileName.substring(fileName.lastIndexOf("."));
        }
        return fileName;
    }

    /**
     * @param fileName
     * @return
     */
    public static String getFileExtensionWithoutDot(String fileName) {
        if (fileName.indexOf(".") != -1) {
            return fileName.substring(fileName.lastIndexOf(".") + 1);
        }
        return fileName;
    }

    public static void deleteFile(String path){
        File img = new File(path);
        if(img.exists()){
            img.delete();
        }
    }

    public static void deleteFile(String dir, String filePath){
        String path = FileUtilsEx.joinPaths(dir, "/", filePath);
        File img = new File(path);
        if(img.exists()){
            img.delete();
        }
    }
}
