package com.chinasofti.ark.bdadp.util.common;


import java.io.IOException;
import org.apache.hadoop.fs.FileSystem;

/**
 * Created by water on 2017.7.17.
 */
public class FileUtils {

  public static String HDFS_PATH = "hdfs://";

  public static void checkDirExists(String path) {

    if (path.contains(HDFS_PATH)) {

      org.apache.hadoop.fs.Path hdfsPath = new org.apache.hadoop.fs.Path(path);
      FileSystem hdfs = null;
      try {
        hdfs = FileSystem.get(
            new java.net.URI(path), new org.apache.hadoop.conf.Configuration());
        if (hdfs.exists(hdfsPath)) {
          hdfs.delete(hdfsPath, true);
        }
      } catch (Exception e) {
        e.printStackTrace();
      }

    } else {
      java.io.File filePath = new java.io.File(path);
      if (filePath.exists()) {
        try {
          org.apache.commons.io.FileUtils.deleteDirectory(filePath);
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
  }

}
