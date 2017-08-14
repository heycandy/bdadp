package com.chinasofti.ark.bdadp.util.common;


import java.io.File;
import java.io.IOException;
import java.net.URI;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

/**
 * Created by water on 2017.7.17.
 */
public class FileUtils extends org.apache.commons.io.FileUtils{

  public static String HDFS_PATH = "hdfs://";

  public static void checkDirExists(String path) {

    if (path.contains(HDFS_PATH)) {
      Path hdfsPath = new org.apache.hadoop.fs.Path(path);
      FileSystem hdfs = null;
      try {
        hdfs = FileSystem.get(
            new URI(path), new Configuration());
        if (hdfs.exists(hdfsPath)) {
          hdfs.delete(hdfsPath, true);
        }
      } catch (Exception e) {
        e.printStackTrace();
      }

    } else {
      File filePath = new File(path);
      if (filePath.exists()) {
        try {
          deleteDirectory(filePath);
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
  }

}
