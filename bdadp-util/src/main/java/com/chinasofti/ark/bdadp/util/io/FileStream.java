package com.chinasofti.ark.bdadp.util.io;

import com.chinasofti.ark.bdadp.util.hdfs.common.ConfigurationClient;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import java.io.*;

/**
 * Created by Administrator on 2016/11/10.
 */
public class FileStream {

    public InputStream createInStream(String inPath, String fileSource) throws IOException {
        if ("0".equals(fileSource)) {
            return new FileInputStream(inPath);
        } else {
            FileSystem fs = FileSystem.get(ConfigurationClient.getInstance().getConfiguration());
            return fs.open(new Path(inPath));
        }
    }

    public OutputStream createOutputStream(String outPath, String destSource) throws IOException {
        if ("0".equals(destSource)) {
            return new FileOutputStream(outPath);
        } else {
            FileSystem fs = FileSystem.get(ConfigurationClient.getInstance().getConfiguration());
            return fs.create(new Path(outPath));
        }
    }
}
