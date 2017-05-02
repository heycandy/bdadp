package com.chinasofti.ark.bdadp.service.tools;

import com.chinasofti.ark.bdadp.util.common.StringUtils;
import com.chinasofti.ark.bdadp.util.hadoop.HdfsMain;
import com.chinasofti.ark.bdadp.util.hdfs.common.HDFSClient;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by msc on 2016/9/18.
 */
@Service
public class HdfsInfoService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * findAll hdfs file
     */
    public Map<String, Boolean> listFiles(String hdfspath) throws IllegalArgumentException,
            IOException {
        if (StringUtils.isNulOrEmpty(hdfspath) || !hdfspath.startsWith("/") || hdfspath
                .startsWith("//")) {
            logger.error("Input path[{}] error, need to be absolute path starting with '/'", hdfspath);
            throw new RuntimeException(
                    "Input path error, need to be absolute path starting with '/', input: " + hdfspath);
        }
        int fsNameLen = HDFSClient.getFsName().length();
        Map<String, Boolean> rsp = new HashMap<>();// key is true if target is
        FileStatus[] files = HDFSClient.getFileSystem().listStatus(new Path(hdfspath));
        for (FileStatus file : files) {
            rsp.put(file.getPath().toString().substring(fsNameLen), file.isDirectory());
        }
        logger.info("Sub files under directory[{}]: {}", hdfspath, "");
        return rsp;
    }

    /**
     * file upload
     */
    public Boolean uploadFile(InputStream is, String path) {

        try {
            new HdfsMain().write(is, path);
            return true;
        } catch (IOException e) {
            logger.error("File [{}] upload to hdfs [{}] fiald!", path);
            e.printStackTrace();
            return false;
        }
    }

    /**
     * delete file
     */
    public void delFile(String filePath) {
        new HdfsMain().rmdir(filePath);
    }

    /**
     * @param filePath
     * @return
     * @throws IOException
     */
    public String readFile(String filePath) throws IOException {
        String message = new HdfsMain().read(filePath);
        logger.info("File [{}] read for hdfs [{}]!", filePath);
        return message;
    }
}
