package com.chinasofti.ark.bdadp.util.io;

import org.apache.commons.io.filefilter.WildcardFileFilter;

import java.io.File;

/**
 * Created by Administrator on 2016/9/26.
 */
public class MyFileFilter {

    public String filePath;
    public String fileName;

    public MyFileFilter(String filePath, String fileName) {
        this.filePath = filePath;
        this.fileName = fileName;
    }

    public String[] fileFilterFun() {
        File dir = new File(filePath);
        WildcardFileFilter fileFilter = new WildcardFileFilter(fileName);
        return dir.list(fileFilter);
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
