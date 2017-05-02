package com.chinasofti.ark.bdadp.util.hadoop;

//package com.huawei.bigdata.hdfs.examples;

import com.chinasofti.ark.bdadp.util.hdfs.common.HDFSClient;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.security.UserGroupInformation;

import java.io.*;

public class HdfsMain {

    //	private String DEST_PATH = "/user/hdfs-examples";
//	private String FILE_NAME = "test.txt";
    private static String PRINCIPAL = "username.client.kerberos.principal";
    private static String KEYTAB = "username.client.keytab.file";
    private static String confDirPath = HdfsMain.class.getClassLoader().getResource("").getPath();
    private FileSystem fSystem; /* HDFS file system */
    private Configuration conf;
    private FileSystem fs;

    public HdfsMain() {
        //initHDFS(hdfsUser);
        fs = HDFSClient.getFileSystem();
    }

    /**
     * HDFS operator instance
     */
    public void initHDFS(String hdfsUser) {
        // init HDFS FileSystem instance
        try {
            loadConf();        //Add configuration file
            authentication(hdfsUser);        //kerberos security authentication
            instanceBuild();        //build HDFS instance
        } catch (IOException e) {
            System.err.println("Init hdfs filesystem failed. " + e);
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Add configuration file
     */
    private void loadConf() throws IOException {

        conf = new Configuration();
        // conf file
//		conf.addResource(new Path(System.getProperty("user.dir") + File.separator + "conf" + File.separator + "hdfs-site.xml"));
//		conf.addResource(new Path(System.getProperty("user.dir")+ File.separator + "conf" + File.separator + "core-site.xml"));
        conf.addResource(confDirPath + "hdfs-site.xml");
        conf.addResource(confDirPath + "core-site.xml");
    }

    /**
     * kerberos security authentication
     */
    private void authentication(String hdfsUser) throws IOException {
        // security mode
        if ("kerberos".equalsIgnoreCase(conf.get("hadoop.security.authentication"))) {
            conf.set(PRINCIPAL, hdfsUser + "@HADOOP.COM");
            // keytab file
//			conf.set(KEYTAB, System.getProperty("user.dir") + File.separator + "conf" + File.separator + "user.keytab");
            conf.set(KEYTAB, confDirPath + "user.keytab");

            // kerberos path
            String krbFilePath = confDirPath + "krb5.conf";
            System.setProperty("java.security.krb5.conf", krbFilePath);

            //login
            UserGroupInformation.setConfiguration(conf);
            try {
                UserGroupInformation.loginUserFromKeytab(conf.get(PRINCIPAL), conf.get(KEYTAB));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * build HDFS instance
     */
    private void instanceBuild() throws IOException {
        // get filesystem
        try {
            fSystem = FileSystem.get(conf);
        } catch (IOException e) {
            throw new IOException("Get fileSystem failed.");
        }
    }

    /**
     * delete directory
     */
    public void rmdir(String hdfsFilePath) {
        Path destPath = new Path(hdfsFilePath);
        if (!deletePath(destPath)) {
            System.err.println("failed to delete destPath " + hdfsFilePath);
            return;
        }
        System.out.println("succee to delete path " + hdfsFilePath);
    }

    /**
     * create directory
     */
    private void mkdir(String hdfsFilePath) {
        Path destPath = new Path(hdfsFilePath);
        if (!createPath(destPath)) {
            System.err.println("failed to create destPath " + hdfsFilePath);
            return;
        }
        System.out.println("succee to create path " + hdfsFilePath);
    }

    /**
     * create file,write file
     */
    public String write(InputStream in, String hdfsFilePath) throws IOException {
//		final String content = "hi, I am bigdata. It is successful if you can see me.";
//		InputStream in = (InputStream) new ByteArrayInputStream(
//				content.getBytes());
        try {
            HdfsWriter writer = new HdfsWriter(fs, hdfsFilePath);
            writer.doWrite(in);
            System.out.println("success to write.");
        } catch (ParameterException e) {
            e.printStackTrace();
        } catch (IOException e2) {
            e2.printStackTrace();
            System.err.println("failed to write.");
        } finally {
            close(in);
        }

        return hdfsFilePath;
    }

    /**
     * append file content
     */
    public void append(String hdfsFilePath) throws IOException {
        final String content = "I append this content.";
        InputStream in = (InputStream) new ByteArrayInputStream(
                content.getBytes());
        try {
            HdfsWriter writer = new HdfsWriter(fSystem, hdfsFilePath);
            writer.doAppend(in);
            System.out.println("success to append.");
        } catch (ParameterException e) {
            e.printStackTrace();
        } catch (IOException e2) {
            e2.printStackTrace();
            System.err.println("failed to append.");
        } finally {
            close(in);
        }
    }

    /**
     * read file
     */
    public String read(String hdfsFilePath) throws IOException {
        Path path = new Path(hdfsFilePath);
        FSDataInputStream in = null;
        BufferedReader reader = null;
        StringBuffer strBuffer = new StringBuffer();
        Integer readNum = 50;

        try {
            in = fs.open(path);
            reader = new BufferedReader(new InputStreamReader(in));
            String sTempOneLine;

            // write file
            while ((sTempOneLine = reader.readLine()) != null) {
                if (readNum > 100) {
                    break;
                }
                strBuffer.append(sTempOneLine);
                readNum++;
            }
            System.out.println("result is : " + strBuffer.toString());
            System.out.println("success to read.");

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("failed to read.");
        } finally {
            close(reader);
            close(in);
        }
        return strBuffer.toString();
    }

    public void download(String hdfsPath, String localPath) throws IOException {
        FSDataInputStream fsdi = fs.open(new Path(hdfsPath));
        OutputStream output = new FileOutputStream(localPath);
        IOUtils.copyBytes(fsdi, output, 4096, true);
    }

    /**
     * delete file
     */
    private void delete(String hdfsFilePath) throws IOException {
        Path beDeletedPath = new Path(hdfsFilePath);
        try {
            fs.deleteOnExit(beDeletedPath);
            System.out.println("succee to delete the file " + hdfsFilePath);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("failed to delete the file " + hdfsFilePath);
        }
    }

    /**
     * close stream
     */
    private void close(Closeable stream) {
        try {
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * create file path
     */
    private boolean createPath(final Path filePath) {
        try {
            if (!fs.exists(filePath)) {
                fs.mkdirs(filePath);
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * delete file path
     */
    private boolean deletePath(final Path filePath) {
        try {
            if (!fs.exists(filePath)) {
                return true;
            }

            fs.delete(filePath, true);

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

}
