package com.chinasofti.ark.bdadp.util.hdfs.common;

import com.chinasofti.ark.bdadp.util.hadoop.HdfsMain;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;

public class HDFSClient {

    private static final Logger LOG = LoggerFactory.getLogger(HDFSClient.class);

    private static HDFSClient instance;

    private static FileSystem fs;

    private static String fsName;

    private static Configuration CONF;

    static {
        try {
            init();
        } catch (Throwable e) {
            LOG.error("Hdfs client init error: ", e);
            throw new RuntimeException("Hdfs client init error: ", e);
        }
    }

    private HDFSClient() {
        try {
            fsName = fs.getUri().toString();
        } catch (Exception e) {
            LOG.error("Init hfds client error: ", e);
            throw new RuntimeException("Init hdfs client error: ", e);
        }
    }

    public static FileSystem getFileSystem() {
        if (instance == null) {
            synchronized (HDFSClient.class) {
                if (instance == null) {
                    instance = new HDFSClient();
                }
            }
        }
        return fs;
    }

    /**
     * Get the file system name.
     */
    public static String getFsName() {
        if (fsName == null) {
            HDFSClient.getFileSystem();
        }
        return fsName;
    }

    private static void init() throws IOException, URISyntaxException {
        CONF = ConfigurationClient.getInstance().getConfiguration();
        fs = FileSystem.get(CONF);
    }

    public static void main(String[] args)
            throws FileNotFoundException, IllegalArgumentException, IOException {
        HdfsMain h = new HdfsMain();
        h.rmdir("/!#!#!$@!$");

	 /* HdfsMain hdfsMain = new HdfsMain();
          File newFile = new File("C:\\Users\\Administrator\\Desktop\\aa.sh");
	  InputStream is = new FileInputStream(newFile);
	  hdfsMain.write(is,"/tmp/aa.sh");*/

        FileStatus[] status = HDFSClient.getFileSystem().listStatus(new Path("/tmp/"));
        for (FileStatus file : status) {
            System.out.println(">>> path: " + file.getPath());
        }
        //HDFSClient.getFileSystem().delete(new Path("/tmp/testupload"), true);
        //System.out.println(">>> end of main.");
    }
}
