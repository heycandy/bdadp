package com.chinasofti.ark.bdadp.service.tools.bean;

import com.chinasofti.ark.bdadp.service.queue.bean.AbstractQueueTask;
import com.chinasofti.ark.bdadp.util.hdfs.common.HDFSClient;
import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;

/**
 * Created by msc on 2016/11/25.
 */
public class HdfsDownLoadTask extends AbstractQueueTask {

    private static FileSystem fs;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    protected double progress;
    private java.nio.file.Path downLoadPath;
    private String filePath;
    private int dirNum;

    public HdfsDownLoadTask(String id, String name, java.nio.file.Path path, String filePath) {
        super(id, name);
        this.filePath = filePath;
        this.downLoadPath = path;
        fs = HDFSClient.getFileSystem();
    }

    @Override
    public double getProgress() {
        return progress;
    }

    public void setProgress(double progress) {
        this.progress += progress;
        super.reportAll();
    }

    @Override
    public String getAction() {
        return "tools.export";
    }

    @Override
    public void run() {
        dirNum = 1;
        zip(downLoadPath.toString(), filePath);
    }

    @Override
    public boolean remove() {
        try {
            return Files.deleteIfExists(downLoadPath) &&
                    Files.deleteIfExists(downLoadPath.getParent()) &&
                    Files.deleteIfExists(downLoadPath.getParent().getParent());
        } catch (IOException e) {
            return false;
        }
    }

    public void zip(String destFile, String zipDir) {
        File outFile = new File(destFile);
        ArchiveOutputStream out = null;
        String value = null;
        try {
            outFile.createNewFile();
            //创建文件
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(
                    new FileOutputStream(outFile));
            out = new ArchiveStreamFactory()
                    .createArchiveOutputStream(ArchiveStreamFactory.ZIP,
                            bufferedOutputStream);
            this.setProgress(0.1);
            value = zipDir.substring(0, zipDir.lastIndexOf("/") + 1);
            logger.info("fileName " + this.getName());
            myWrite(zipDir, out, value);
            out.finish();
            out.close();
            this.setProgress(1 - (dirNum + 1) * 0.1);
        } catch (IOException e) {
            logger.error("Create file failed !" + e);
        } catch (ArchiveException e) {
            logger.error("compression error" + e);
        } catch (Exception e) {
            logger.error("operation failed" + e);
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        logger.info("zip file finished!");
        this.progress = 1.0;
    }

    public void myWrite(String src, ArchiveOutputStream out, String str) throws Exception {
        if (dirNum < 9) {
            this.setProgress(0.1);
        }
        Path path = new Path(src);
        FileStatus[] fst = fs.listStatus(path);
        FSDataInputStream fsdis = null;
        String parent = null;
        int fsNameLen = fs.getUri().toString().length();
        if (fst.length > 0) {
            for (int i = 0; i < fst.length; i++) {
                FileStatus fileStatus = fst[i];
                String filePath = fileStatus.getPath().toString().substring(fsNameLen);
                if (fst[i].isDirectory()) {
                    dirNum += dirNum;
                    logger.info("filePath " + filePath);
                    myWrite(filePath, out, str);
                } else {
                    fsdis = fs.open(fst[i].getPath());
                    if (str.length() > 1) {
                        parent = filePath.replace(str, "");
                    } else {
                        parent = filePath.substring(1, filePath.length());
                    }
                    String child = fileStatus.getPath().getName();
                    File file = null;
                    if (dirNum > 2) {
                        file = new File(parent, child);
                    } else {
                        file = new File(child);
                    }
                    ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry(file, parent);
                    out.putArchiveEntry(zipArchiveEntry);
                    org.apache.commons.compress.utils.IOUtils.copy(fsdis, out);
                    out.closeArchiveEntry();
                    IOUtils.closeStream(fsdis);
                }
            }
        }
    }
}
