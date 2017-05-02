package com.chinasofti.ark.bdadp.util.jdbc.pool;

import com.chinasofti.ark.bdadp.util.hadoop.HiveJdbcUtil;
import com.chinasofti.ark.bdadp.util.hdfs.common.ConfigurationClient;
import com.google.common.base.Charsets;
import com.google.common.io.Files;
import org.apache.hadoop.conf.Configuration;

import java.io.File;
import java.io.Reader;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * 链接池
 *
 * @author msc
 */
public class JDBCConnPool {

    /**
     * 最大链接池
     */
    private static final int POOL_MAX_SIZE = 2;
    /**
     * 最小链接池
     */
    private static final int POOL_MIN_SIZE = 1;
    /**
     * 链接池对象
     */
    private static List<Connection> pool;
    /**
     * configer
     */
    private Configuration CONF;

    public JDBCConnPool() throws Exception {
        initPool();
    }

    public void initPool() throws Exception {
        CONF = ConfigurationClient.getInstance().getConfiguration();
        if (pool == null) {
            pool = new ArrayList<Connection>();
        }

        Properties props = new Properties();
        File file = Paths.get(System.getProperty("user.hadoop.conf"), "hiveclient.properties").toFile();
        Reader reader = Files.newReader(file, Charsets.UTF_8);
        props.load(reader);

        String zkQuorum = props.getProperty("zk.quorum");
        String userPrincipal = System.getProperty("user.principal");


        while (pool.size() < JDBCConnPool.POOL_MIN_SIZE) {

            pool.add(HiveJdbcUtil.getConnection(zkQuorum, userPrincipal, ""));
        }
    }

    public synchronized Connection getConn() {
        int last_index = pool.size() - 1;
        Connection connection = pool.get(last_index);
        pool.remove(last_index);
        return connection;
    }

    public synchronized void close(Connection conn) {
        if (pool.size() > JDBCConnPool.POOL_MAX_SIZE) {
            try {
                conn.close();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        pool.add(conn);
    }
}
