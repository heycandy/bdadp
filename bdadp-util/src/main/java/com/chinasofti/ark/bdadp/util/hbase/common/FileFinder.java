package com.chinasofti.ark.bdadp.util.hbase.common;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * File finder to load file.
 */
public class FileFinder {

    public static String findFile(String path) {
        File file = new File(path);
        if (file.exists()) {
            System.out.println("File exists! " + path);
            return file.getAbsolutePath();
        }
        String fileName = path.indexOf("/") > -1 ? path.substring(path.lastIndexOf("/") + 1) : path;
        URL url = FileFinder.class.getClassLoader().getResource(fileName);
        System.out.println("File url: " + url.toString());
        return url != null ? url.getPath() : null;
    }

    @SuppressWarnings("deprecation")
    public static URL getFileURL(String path) {
        File file = new File(path);
        if (file.exists()) {
            try {
                System.out.println("File found by relative path! " + path);
                return file.toURL();
            } catch (MalformedURLException e) {
                return null;
            }
        }
        String fileName = path.indexOf("/") > -1 ? path.substring(path.lastIndexOf("/") + 1) : path;
        URL url = FileFinder.class.getClassLoader().getResource(fileName);
        System.out.println("File found by class loader: " + url);
        return url != null ? url : null;
    }

}
