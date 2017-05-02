package com.chinasofti.ark.bdadp.util.hbase.common;

import com.chinasofti.ark.bdadp.util.hdfs.common.ConfigurationClient;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Table;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;


/**
 * H-connection and table poll are wrapped in this class.
 */
public class HbaseClient {

    private static final Logger LOG = LoggerFactory.getLogger(HbaseClient.class);

    private static Configuration CONF;

    private static Connection conn;

    static {
        try {
            CONF = ConfigurationClient.getInstance().getConfiguration();
            connect();
        } catch (Throwable e) {
            LOG.error("Hbase client init error: ", e);
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private HbaseClient() {
    }

    /**
     * Client should be responsible for closing the returned admin afterwards.
     */
    public static Admin getAdmin() {
        try {
            return conn.getAdmin();
        } catch (IOException e) {
            LOG.error("HbaseAdmin init error: ", e);
            throw new RuntimeException("Init habseadmin error. ", e);
        }
    }

    private static void connect() throws IOException {
        conn = ConnectionFactory.createConnection(CONF);
    }

    public static Table getHTable(String tableName) {
        Table htab;
        try {
            htab = conn.getTable(TableName.valueOf(tableName));
            return htab;
        } catch (IOException e) {
            LOG.error("HPool get table [{}] error: ", tableName, e);
            throw new RuntimeException("HPool get table error.");
        }
    }

    public static void main(String[] args) throws IOException {

        Admin administrator = HbaseClient.getAdmin();
        HTableDescriptor[] tables = administrator.listTables();
        for (HTableDescriptor tab : tables) {
            System.out.println(">>>table: " + tab.getNameAsString());
        }

        administrator.close();
        System.out.println(">>> end main.");
    }
}
