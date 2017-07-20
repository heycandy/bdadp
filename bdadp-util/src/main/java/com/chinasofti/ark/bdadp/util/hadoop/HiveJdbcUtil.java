package com.chinasofti.ark.bdadp.util.hadoop;


import com.chinasofti.ark.bdadp.security.SecurityLogin;

import org.apache.hadoop.conf.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/3/18.
 */
public class HiveJdbcUtil {

  /**
   * 所连接的集群是否为安全版本
   */
  private static final boolean isSecureVerson = true;
  private static final String HIVE_DRIVER = "org.apache.hive.jdbc.HiveDriver";
  private static final String HADOOP_CONF_DIR =
      System.getProperty("user.hadoop.conf") + File.separator;
  private static final String USER_KEYTAB_FILE = HADOOP_CONF_DIR + "user.keytab";
  private static final String KRB5_FILE = HADOOP_CONF_DIR + "krb5.conf";
  private static final String ZOOKEEPER_DEFAULT_LOGIN_CONTEXT_NAME = "Client";
  private static final String ZOOKEEPER_SERVER_PRINCIPAL_KEY = "zookeeper.server.principal";
  private static final String ZOOKEEPER_DEFAULT_SERVER_PRINCIPAL = "zookeeper/hadoop";
  /**
   * 所连接的zookeeper需要设置的属性
   */

  private static Logger log = LoggerFactory.getLogger(HiveJdbcUtil.class);
  private static ResultSet res;
//  private static String confDirPath = HiveJdbcUtil.class.getClassLoader().getResource("").getPath();

  public static Connection getConnection(String zkQuorum, String userName, String pwd)
      throws ClassNotFoundException, SQLException, IOException {
    // 其中，zkQuorum的"xxx.xxx.xxx.xxx"为集群中Zookeeper所在节点的IP，端口默认是24002
//    String krb5Conf = System.getProperty("user.hadoop.conf") + File.separator + "krb5.conf";
//    System.setProperty(SECURITY_KRB5_CONF, krb5Conf);
    // 拼接JDBC URL
    StringBuilder sBuilder = new StringBuilder("jdbc:hive2://").append(zkQuorum).append("/");
    if (isSecureVerson) {
      // 安全版
      // Zookeeper登录认证
      SecurityLogin.setJaasConf(ZOOKEEPER_DEFAULT_LOGIN_CONTEXT_NAME, userName, USER_KEYTAB_FILE);
      SecurityLogin.setZookeeperServerPrincipal(ZOOKEEPER_SERVER_PRINCIPAL_KEY,
                                                ZOOKEEPER_DEFAULT_SERVER_PRINCIPAL);

      Configuration conf = new Configuration();
      SecurityLogin.login(userName, USER_KEYTAB_FILE, KRB5_FILE, conf);
      // 在使用多实例特性时append("hiveserver2;sasl.qop=auth-conf;auth=KERBEROS;principal=hive/hadoop.hadoop.com@HADOOP.COM")的"hiveserver2"与"hive/hadoop.hadoop.com@HADOOP.COM"根据使用不同的实例进行变更
      // 例如使用Hive1实例则改成"hiveserver2_1"与"hive1/hadoop.hadoop.com@HADOOP.COM"，Hive2实例为"hiveserver2_2",以此类推。
      sBuilder.append(";serviceDiscoveryMode=")
          .append("zooKeeper")
          .append(";zooKeeperNamespace=")
          .append(
              "hiveserver2;sasl.qop=auth-conf;auth=KERBEROS;principal=hive/hadoop.hadoop.com@HADOOP.COM")
          .append(";");
    } else {
      // 非安全版
      // 使用多实例特性的"hiveserver2"变更参照安全版
      sBuilder.append(";serviceDiscoveryMode=")
          .append("zooKeeper")
          .append(";zooKeeperNamespace=")
          .append("hiveserver2;auth=none");
    }
    String url = sBuilder.toString();
    System.out.println(url);

    // 加载Hive JDBC驱动
    Class.forName(HIVE_DRIVER);

    // 获取JDBC连接
    Connection connection = DriverManager.getConnection(url, null, null);

    return connection;
  }

  public static void closeConnection(Connection connection) {
    if (null != connection) {
      try {
        connection.close();
      } catch (SQLException sqle) {
        sqle.printStackTrace();
      }
    }
  }

  public static void main(String[] args) throws SQLException, ClassNotFoundException {

    try {
      Connection
          connection =
          getConnection("10.100.66.116:24002,10.100.66.117:24002,10.100.66.118:24002", "hive_hbase",
                        "");
      List<String> resultList = getDbNameList(connection);
      List
          resultMap =
          execHql1(connection, "default", "show tables;", 1000);
      System.out.println(resultMap.toString());

    } catch (Exception e) {
      e.printStackTrace();
    }

  }

  private static void countData(Statement stmt, String tableName) throws SQLException {
    String sql = "select count(1) from " + tableName;
    log.info("Running:" + sql);
    res = stmt.executeQuery(sql);
    log.info("执行“regular hive query”运行结果:");
    while (res.next()) {
      log.info("count ------>" + res.getString(1));
    }
  }

  private static void selectData(Statement stmt, String tableName) throws SQLException {
    String sql = "select * from " + tableName;
    log.info("Running:" + sql);
    res = stmt.executeQuery(sql);
    log.info("执行 select * query 运行结果:");
    while (res.next()) {
      log.info(res.getInt(1) + "\t" + res.getString(2));
    }
  }

  private static void loadData(Statement stmt, String tableName, String filepath)
      throws SQLException {

    String sql = "load data inpath '" + filepath + "' into table " + tableName;
    log.info("Running:" + sql);
    res = stmt.executeQuery(sql);
  }

  private static void describeTables(Statement stmt, String tableName) throws SQLException {
    String sql = "describe " + tableName;
    log.info("Running:" + sql);
    res = stmt.executeQuery(sql);
    log.info("执行 describe table 运行结果:");
    while (res.next()) {
      log.info(res.getString(1) + "\t" + res.getString(2));
    }
  }

  private static void showTable(Statement stmt, String tableName) throws SQLException {
    String sql = "show tables '" + tableName + "'";
    log.info("Running:" + sql);
    res = stmt.executeQuery(sql);
    log.info("执行 show tables 运行结果:");
    if (res.next()) {
      log.info(res.getString(1));
    }
  }

  public static String createTable(Statement stmt, String dbName, String sql) throws SQLException {
    log.info("执行： " + sql);
    if (dbName != null && !"".equals(dbName)) {
      stmt.execute("use " + dbName);
    }
    stmt.execute(sql);
    String tableName = InputDataUtil.getSplitLastRightStr(sql.split("\\(")[0]);
    return tableName;
  }

  private static String dropTable(Statement stmt) throws SQLException {
    // 创建的表名
    String tableName = "testHive";
    String sql = "drop table " + tableName;
    log.info("执行：" + sql);
    stmt.executeQuery(sql);
    return tableName;
  }

  public static boolean execute(PreparedStatement preparedStatement) throws SQLException {
    preparedStatement.execute();
    return true;

  }

  public static void showDbs(Statement stmt) throws SQLException {
    String sql = "show databases ";
    // show tables
    System.out.println("Running: " + sql);
    ResultSet res = stmt.executeQuery(sql);
    if (res.next()) {
      System.out.println(res.getString(1));
    }
  }

  public static void showTables(Statement stmt, String dbName) throws SQLException {
    stmt.executeQuery("use " + dbName);
    String sql = "show tables ";
    // show tables
    System.out.println("Running: " + sql);
    ResultSet res = stmt.executeQuery(sql);
    if (res.next()) {
      System.out.println(res.getString(1));
    }
  }

  public static List<String> getDbNameList(Connection connection) throws SQLException {
    List<String> resultList = new ArrayList<String>();
    String sql = "show databases";

    PreparedStatement statement = null;
    ResultSet resultSet = null;
    try {
      // 执行HQL
      statement = connection.prepareStatement(sql);
      resultSet = statement.executeQuery();

      while (resultSet.next()) {
        resultList.add(resultSet.getString(1));
      }
    } finally {
      if (null != resultSet) {
        resultSet.close();
      }

      if (null != statement) {
        statement.close();
      }
    }
    return resultList;
  }

  public static List<String> getTableNameList(Connection connection, String dbName)
      throws SQLException {
    List<String> resultList = new ArrayList<String>();
    String[] sqls = {"use " + dbName, "show tables"};

    execDDL(connection, dbName, sqls[0]);

    PreparedStatement statement = null;
    ResultSet resultSet = null;
    try {
      // 执行HQL
      statement = connection.prepareStatement(sqls[1]);
      resultSet = statement.executeQuery();

      while (resultSet.next()) {
        resultList.add(resultSet.getString(1));
      }
    } finally {
      if (null != resultSet) {
        resultSet.close();
      }
      if (null != statement) {
        statement.close();
      }
    }
    System.out.println(resultList);
    return resultList;
  }

  public static List<String> getTableDescInfoList(Connection connection, String dbName,
                                                  String tableName)
      throws SQLException {
    List<String> resultList = new ArrayList<String>();
    String[] sqls = {"use " + dbName, "desc " + tableName};

    execDDL(connection, null, sqls[0]);

    PreparedStatement statement = null;
    ResultSet resultSet = null;
    try {
      // 执行HQL
      statement = connection.prepareStatement(sqls[1]);
      resultSet = statement.executeQuery();
      while (resultSet.next()) {
        resultList.add(resultSet.getString(1) + "(" + resultSet.getString(2) + ")");
      }
    } finally {
      if (null != resultSet) {
        resultSet.close();
      }

      if (null != statement) {
        statement.close();
      }
    }
    return resultList;
  }

  @SuppressWarnings("resource")
  public static Map<String, List> execHql(Connection connection, String dbName, String hqlStats,
                                          int maxRows) throws SQLException {
    HashMap resultMap = new HashMap<String, List>();

    String[] hqls = hqlStats.split(";");

    PreparedStatement statement = null;
    ResultSet resultSet = null;
    ResultSetMetaData resultMetaData = null;

    execDDL(connection, null, "use " + dbName);
    for (String hql : hqls) {
      List<List> rsList = new ArrayList<List>();
      if ("".equals(hql.trim())) {
        continue;
      }

      try {
        // 执行HQL
        statement = connection.prepareStatement(hql);
        if (statement.execute()) {
          resultSet = statement.getResultSet();
          resultMetaData = resultSet.getMetaData();
          List<String> metaDataList = new ArrayList<String>();
          int columnCount = resultMetaData.getColumnCount();
          String columnLabel = "";
          String[] strArr = null;
          for (int i = 1; i <= columnCount; i++) {
            columnLabel = resultMetaData.getColumnLabel(i);
            strArr = columnLabel.split("\\.");
            metaDataList.add(strArr[strArr.length - 1]);
          }
          rsList.add(metaDataList);

          int iRow = 0;
          while (resultSet.next() && iRow < maxRows) {
            List<String> rowList = new ArrayList<String>();
            for (int i = 1; i <= columnCount; i++) {
              rowList.add(resultSet.getString(i));
            }
            rsList.add(rowList);
            iRow++;
          }
        }

      } catch (Exception e) {
        log.info("Running:" + hql);
        log.error("执行结果:" + e);

        List<Object> rowList = new ArrayList<Object>();
        rowList.add("ERROR");
        rowList.add(e.getMessage());
        rsList.add(rowList);
        System.out.println(e);

        throw new RuntimeException(e);
      } finally {
        if (null != resultSet) {
          resultSet.close();
        }

        if (null != statement) {
          statement.close();
        }
      }
      resultMap.put(hql.trim(), rsList);

    }
    return resultMap;
  }

  @SuppressWarnings("resource")
  public static List<Object> execHql1(Connection connection, String dbName, String hqlStats,
                                      int maxRows) throws SQLException {
    List<Object> returnList = new ArrayList<Object>();

    String[] hqls = hqlStats.split(";");

    PreparedStatement statement = null;
    ResultSet resultSet = null;
    ResultSetMetaData resultMetaData = null;
    int resultCount = 0;

    execDDL(connection, null, "use " + dbName);
    for (String hql : hqls) {
      HashMap resultMap = new HashMap<String, List>();
      List<List> rsList = new ArrayList<List>();
      if ("".equals(hql.trim())) {
        continue;
      }
      String[] list = hql.split(" ");
      int index = -1;
      String mapKey = null;
      if (list[0].trim().equals("select")) {
        for (String sql : list) {
          index++;
          if (sql.equals("from") && list[index + 1].indexOf("(") < 0) {
            mapKey = list[index + 1];
            break;
          } else {
            continue;
          }
        }
      } else {
        mapKey = "output";
      }
      try {
        // 执行HQL
        statement = connection.prepareStatement(hql);
        if (statement.execute()) {
          resultSet = statement.getResultSet();
          resultMetaData = resultSet.getMetaData();
          List<String> metaDataList = new ArrayList<String>();
          int columnCount = resultMetaData.getColumnCount();
          String columnLabel = "";
          String[] strArr = null;
          for (int i = 1; i <= columnCount; i++) {
            columnLabel = resultMetaData.getColumnLabel(i);
            strArr = columnLabel.split("\\.");
            metaDataList.add(strArr[strArr.length - 1]);
          }
          rsList.add(metaDataList);

          int iRow = 0;
          while (resultSet.next() && iRow < maxRows) {
            List<String> rowList = new ArrayList<String>();
            for (int i = 1; i <= columnCount; i++) {
              rowList.add(resultSet.getString(i));
            }
            rsList.add(rowList);
            iRow++;
          }
        } else {
          List<Object> rowList = new ArrayList<Object>();
          resultCount = (int) statement.getUpdateCount();
          rowList.add(resultCount);
          rsList.add(rowList);
        }

      } catch (Exception e) {
        log.info("Running:" + hql);
        log.error("执行结果:" + e);

        List<Object> rowList = new ArrayList<Object>();
        rowList.add("ERROR");
        rowList.add(e.getMessage());
        rsList.add(rowList);
        System.out.println(e);
        resultMap.put(mapKey.trim(), rsList);
        returnList.add(resultMap);
        break;
        //throw new RuntimeException(e);
      } finally {
        if (null != resultSet) {
          resultSet.close();
        }

        if (null != statement) {
          statement.close();
        }
      }
      resultMap.put(mapKey.trim(), rsList);
      returnList.add(resultMap);
    }
    return returnList;
  }


  @SuppressWarnings("resource")
  public static void execDDL(Connection connection, String dbName, String sql) throws SQLException {
    PreparedStatement statement = null;
    try {
      if (dbName != null && !"".equals(dbName)) {
        statement = connection.prepareStatement("use " + dbName);
        statement.execute();
      }
      statement = connection.prepareStatement(sql);
      statement.execute();
    } finally {
      if (null != statement) {
        statement.close();
      }
    }
  }

  public static void execDML(Connection connection, String sql) throws SQLException {
    PreparedStatement statement = null;
    ResultSet resultSet = null;
    ResultSetMetaData resultMetaData = null;

    try {
      // 执行HQL
      statement = connection.prepareStatement(sql);
      resultSet = statement.executeQuery();

      // 输出查询的列名到控制台
      resultMetaData = resultSet.getMetaData();
      int columnCount = resultMetaData.getColumnCount();
      for (int i = 1; i <= columnCount; i++) {
        System.out.print(resultMetaData.getColumnLabel(i) + '\t');
      }
      System.out.println();

      // 输出查询结果到控制台
      while (resultSet.next()) {
        for (int i = 1; i <= columnCount; i++) {
          System.out.print(resultSet.getString(i) + '\t');
        }
        System.out.println();
      }
    } finally {
      if (null != resultSet) {
        resultSet.close();
      }

      if (null != statement) {
        statement.close();
      }
    }
  }

  public static Map<String, List<String>> getTablesAndColumns(Connection connection, String dbName)
      throws SQLException {
    List<String> resultList = null;
    Map<String, List<String>> map = new HashMap<String, List<String>>();
    String[] sqls = {"use " + dbName, "show tables"};

    execDDL(connection, null, sqls[0]);

    PreparedStatement statement = null;
    PreparedStatement st = null;
    ResultSet resultSet = null;
    ResultSet set = null;
    try {
      // 执行HQL
      statement = connection.prepareStatement(sqls[1]);
      resultSet = statement.executeQuery();
      while (resultSet.next()) {
        resultList = new ArrayList<String>();
        //resultList.add(resultSet.getString(1));
        st = connection.prepareStatement("desc " + resultSet.getString(1));
        set = st.executeQuery();
        while (set.next()) {
          resultList.add(set.getString(1) + "(" + set.getString(2) + ")");
        }
        map.put(resultSet.getString(1), resultList);
      }
    } finally {
      if (null != resultSet) {
        resultSet.close();
      }
      if (null != st) {
        st.close();
      }

      if (null != statement) {
        statement.close();
      }
      if (null != set) {
        set.close();
      }
    }
    return map;
  }
}
