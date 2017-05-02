package com.chinasofti.ark.bdadp.util.io;

import java.io.File;
import java.io.FilenameFilter;

/**
 * Created by wumin on 2016/9/21.
 */
public class FileUtil {

    public static File[] filter(File dir, final String filter) {
        return dir.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.contains(filter);
            }
        });
    }

    public static String toPath(String pathStr) {
        return pathStr.endsWith(File.separator) ? pathStr : pathStr + File.separator;
    }

    public static String getExtensionName(String fileName) {
        int dotIndex = checkDot(fileName);
        if (dotIndex == -1) {
            return null;
        }
        return fileName.substring(dotIndex + 1);
    }

    public static String getFileNameNoEx(String fileName) {
        int dotIndex = checkDot(fileName);
        if (dotIndex == -1) {
            return null;
        }
        return fileName.substring(0, dotIndex);
    }

    private static int checkDot(String fileName) {
        if (fileName == null || fileName.isEmpty()) {
            return -1;
        }
        int dotIndex = fileName.lastIndexOf(".");
        if (dotIndex == -1 || !(dotIndex < fileName.length() - 1)) {
            return -1;
        }
        return dotIndex;
    }

}
