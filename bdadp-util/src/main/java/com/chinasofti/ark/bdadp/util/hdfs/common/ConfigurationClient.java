package com.chinasofti.ark.bdadp.util.hdfs.common;

import com.chinasofti.ark.bdadp.security.SecurityLogin;
import com.chinasofti.ark.bdadp.util.hbase.common.FileFinder;
import org.apache.hadoop.conf.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 * Configuration used by hdfs/hbase/mapreduce etc.
 */
public class ConfigurationClient {

    private static final Logger LOG = LoggerFactory.getLogger(ConfigurationClient.class);
    private static final String CORE_SITE = "core-site.xml";
    private static final String HDFS_SITE = "hdfs-site.xml";
    private static final String HBASE_SITE = "hbase-site.xml";
    private static final String MAPRED_SITE = "mapred-site.xml";
    private static final String YARN_SITE = "yarn-site.xml";
    private static final String HIVE_SIZE = "hive-site.xml";

    private static final String ZOOKEEPER_DEFAULT_LOGIN_CONTEXT_NAME = "Client";
    private static final String ZOOKEEPER_SERVER_PRINCIPAL_KEY = "zookeeper.server.principal";
    private static final String ZOOKEEPER_DEFAULT_SERVER_PRINCIPAL = "zookeeper/hadoop";

    private static final String
            HADOOP_CONF_DIR =
            System.getProperty("user.hadoop.conf") + File.separator;

    private static final String USER_KEYTAB_FILE = HADOOP_CONF_DIR + "user.keytab";
    private static final String KRB5_FILE = HADOOP_CONF_DIR + "krb5.conf";

    private static ConfigurationClient instance;

    private Configuration conf = new Configuration();

    private ConfigurationClient() {
        try {
            addResources();
            initClusterSecurity();
        } catch (IOException e) {
            LOG.error("Init hadoop configuration exception: ", e);
            throw new RuntimeException(e);
        }
    }

    public static ConfigurationClient getInstance() {
        if (instance == null) {
            synchronized (ConfigurationClient.class) {
                if (instance == null) {
                    instance = new ConfigurationClient();
                }
            }
        }
        return instance;
    }

    public static boolean isInstance() {
        return instance != null;
    }

    public Configuration getConfiguration() {
        return conf;
    }

    public String get(String name) {
        return conf.get(name);
    }

    public String get(String name, String defaultValue) {
        return conf.get(name, defaultValue);
    }

    private void addResources() throws IOException {
        initBasics(HADOOP_CONF_DIR + CORE_SITE);
        initBasics(HADOOP_CONF_DIR + HDFS_SITE);
        initBasics(HADOOP_CONF_DIR + HBASE_SITE);
        initBasics(HADOOP_CONF_DIR + MAPRED_SITE);
        initBasics(HADOOP_CONF_DIR + YARN_SITE);
        initBasics(HADOOP_CONF_DIR + HIVE_SIZE);
    }

    private void initClusterSecurity() throws IOException {
        if ("kerberos".equalsIgnoreCase(conf.get("hadoop.security.authentication"))) {
            SecurityLogin.setJaasConf(ZOOKEEPER_DEFAULT_LOGIN_CONTEXT_NAME,
                    System.getProperty("user.principal", "hive_hbase"),
                    USER_KEYTAB_FILE);
            SecurityLogin.setZookeeperServerPrincipal(ZOOKEEPER_SERVER_PRINCIPAL_KEY,
                    ZOOKEEPER_DEFAULT_SERVER_PRINCIPAL);

            SecurityLogin
                    .login(System.getProperty("user.principal", "hive_hbase"), USER_KEYTAB_FILE, KRB5_FILE,
                            conf);

        }
    }

    private void initBasics(String path) {
        URL filePath = FileFinder.getFileURL(path);
        if (filePath != null) {
            conf.addResource(filePath);
            return;
        } else {
            LOG.error("File not found: [{}].", path);
        }
        //throw new RuntimeException("File not found: " + path);
    }
}
