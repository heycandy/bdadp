package com.chinasofti.ark.bdadp.service.tools;

import com.chinasofti.ark.bdadp.util.hadoop.HiveJdbcUtil;
import com.google.common.base.Charsets;
import com.google.common.io.Files;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Paths;
import java.sql.*;
import java.util.*;

/**
 * Created by Administrator on 2016/9/22.
 */
@Service
public class HiveInfoService {

//  @Autowired
//  private JDBCConnPool jdbcConnPool;

    private static Connection connection;

    static {
        Properties props = new Properties();

        try {
            File
                    file =
                    Paths.get(System.getProperty("user.hadoop.conf"), "hiveclient.properties").toFile();
            Reader reader = Files.newReader(file, Charsets.UTF_8);
            props.load(reader);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String
                zkQuorum =
                props.getProperty("zk.quorum",
                        "10.100.66.125:24002,10.100.66.124:24002,10.100.66.123:24002");
        String userPrincipal = System.getProperty("user.principal", "hive_hbase");

        try {
            connection = HiveJdbcUtil.getConnection(zkQuorum, userPrincipal, "");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<String> getDbNameList()
            throws ClassNotFoundException, SQLException, IOException {
        List<String> resultList = null;
//    Connection connection = jdbcConnPool.getConn();
        try {
            resultList = HiveJdbcUtil.getDbNameList(connection);
        } finally {
            // 关闭JDBC连接
        }
        return resultList;
    }

    public Map<String, List<String>> getTablesAndColumns(String dbName)
            throws SQLException, ClassNotFoundException, IOException {
        Map<String, List<String>> map = new HashMap<String, List<String>>();
        try {
            map = HiveJdbcUtil.getTablesAndColumns(connection, dbName);
        } finally {
            // 关闭JDBC连接
        }
        return map;
    }

    public List<String> getTableNameList(String dbName)
            throws SQLException, ClassNotFoundException, IOException {
        List<String> resultList = null;
        // ServiceCompConnEntity scce = getServiceCompConnByIds(clusterId,
        // srvCompId);
        try {
            resultList = HiveJdbcUtil.getTableNameList(connection, dbName);
        } finally {
            // 关闭JDBC连接
        }
        return resultList;
    }

    public List<String> getTableDescInfoList(String dbName, String tableName)
            throws SQLException, ClassNotFoundException, IOException {
        List<String> resultList = null;
        // ServiceCompConnEntity scce = getServiceCompConnByIds(clusterId,
        // srvCompId);
        try {
            resultList = HiveJdbcUtil.getTableDescInfoList(connection, dbName, tableName);
        } finally {
            // 关闭JDBC连接
        }
        return resultList;
    }

    public List<Object> execHql1(String dbName, String hqlStats, int maxRows)
            throws SQLException, ClassNotFoundException, IOException {
        List<Object> resultMap = null;
        try {
            resultMap = HiveJdbcUtil.execHql1(connection, dbName, hqlStats, maxRows);
        } finally {
            // 关闭JDBC连接
        }
        return resultMap;
    }


    public void hive2Hbase(String hiveTableName, Map<String, List<String>> map) {

        byte[] familyName = Bytes.toBytes("f1");
        List<Put> puts = new ArrayList<Put>();
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        ResultSetMetaData resultMetaData = null;
        DatabaseMetaData dbMeta = null;
        List<byte[]> list = new ArrayList<>();
        //execDDL(connection, null, "use default");

        try {
            statement = connection.prepareStatement("select * from  e_tbl_tbpc1010");
            if (statement.execute()) {
                resultSet = statement.getResultSet();
                resultMetaData = resultSet.getMetaData();
                dbMeta = connection.getMetaData();
                ResultSet pkRSet = dbMeta.getPrimaryKeys(null, null, "e_tbl_tbpc1010");
                int columnCount = resultMetaData.getColumnCount();
                String columnLabel = "";
                String[] strArr = null;
                for (int i = 1; i <= columnCount; i++) {
                    columnLabel = resultMetaData.getColumnName(1);//.getColumnLabel(i);
                    strArr = columnLabel.split("\\.");
                    list.add(Bytes.toBytes(strArr[strArr.length - 1]));
                }

                int iRow = 0;
                while (resultSet.next() && iRow < 10000) {
                    Put put = new Put(Bytes.toBytes("012005000201"));
                    //byte[] value = Bytes.toBytes(resultSet.getString(i));
                    for (int i = 1; i <= columnCount; i++) {
                        //rowList.add(Bytes.toBytes(resultSet.getString(i)));
                        put.addColumn(familyName, list.get(0), Bytes.toBytes(resultSet.getString(i)));
                    }
                    puts.add(put);
                    iRow++;
                }
                //testPut(puts);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // 关闭JDBC连接
        }
    }


}
