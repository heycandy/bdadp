package com.chinasofti.ark.bdadp.util.hadoop;


import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by Administrator on 2016/3/3.
 */
public class InputDataUtil {

    private static org.slf4j.Logger log = LoggerFactory.getLogger(InputDataUtil.class);

//    public static boolean createTable(Connection connection,String createHql){

//        PreparedStatement preparedStatement = null;
//        try {
//            log.info("execute hql :" +createHql);
//            preparedStatement = connection.prepareStatement(createHql);
//            preparedStatement.execute();
//            return true;
//        }catch (SQLException e){
//            e.printStackTrace();
//            return false;
//        }

//    }

    public static boolean execHQL(Connection connection, String HQL) {

        try {
            log.info("execute hql :" + HQL);
            Statement stm = connection.createStatement();
            stm.execute(HQL);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    public static String genHiveHbaseMappingTableDDL(String queryHql, String hiveTable,
                                                     String hbaseTable) {
        int start = queryHql.toUpperCase().indexOf("SELECT");
        int end = queryHql.toUpperCase().indexOf("FROM");
        String feilds = queryHql.substring(start + 6, end);
        String[] fieldList = feilds.split(",");

        StringBuffer sb = new StringBuffer();
        sb.delete(0, sb.length());
        sb.append("CREATE TABLE IF NOT EXISTS " + hbaseTable + " ( ");

        String cols = "";
        for (int i = 0; i < fieldList.length; i++) {
            sb.append(" " + getSplitLastRightStr(fieldList[i]) + " string ");
            if (i + 1 < fieldList.length) {
                sb.append(",");
            }
            if (i != 0) {
                cols = cols + fieldList[i] + ":" + i;
                if (i + 1 < fieldList.length) {
                    cols = cols + ", ";
                }
            }
        }
        sb.append(")");
        sb.append(" STORED BY 'org.apache.hadoop.hive.hbase.HBaseStorageHandler' ");
        sb.append(" WITH SERDEPROPERTIES ('hbase.columns.mapping' = ':key," + cols + " ')   ");
        sb.append(" TBLPROPERTIES ('hbase.table.name' = '" + hbaseTable + "') ");

        return sb.toString();
    }


    public static String getSplitLastRightStr(String srcStr) {
        String[] strList = srcStr.trim().split("\\s+");
        return strList[strList.length - 1];
    }


    public static String loadData2HDFS(String hdfsUser, String hdfsFilePath, String srcFilePath)
            throws Exception {
        InputStream is = null;
        String path = null;
        try {
            is = new FileInputStream(new File(srcFilePath));
            //path = new HdfsMain(hdfsUser).write(is, hdfsFilePath);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException ie) {
                    ie.printStackTrace();
                }
            }
        }
        return path;
    }

}
