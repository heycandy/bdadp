package com.chinasofti.ark.bdadp.service.tools;

import com.chinasofti.ark.bdadp.util.hbase.common.HbaseClient;
import com.chinasofti.ark.bdadp.util.hbase.common.HbaseShellEntity;
import com.chinasofti.ark.bdadp.util.hbase.executor.HbaseShellExecutor;
import com.chinasofti.ark.bdadp.util.hbase.executor.ScanConstructor;
import com.chinasofti.ark.bdadp.util.hbase.response.HbaseInfoRsp;
import com.chinasofti.ark.bdadp.util.hbase.response.HbaseQueryRsp;
import com.chinasofti.ark.bdadp.util.hbase.response.HbaseShellRsp;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

/**
 * Created by msc on 2016/9/27.
 */
@Service
public class HbaseInfoService {

    private static final Logger LOG = LoggerFactory.getLogger(HbaseInfoService.class);

    private final int RSP_THREASHOLD = 200; // max response number to return to client

    public HbaseInfoService() {
        try {
            HbaseClient.getAdmin().close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        //HbaseInfoRsp rsp = new HbaseInfoService().fetchHbaseTables();
        HbaseQueryRsp rsp = new HbaseInfoService().query("hbase_simple_table", "012005000202",
                "012005000205");
        List<HbaseQueryRsp.HbaseKV> list = rsp.getKvs();
        for (HbaseQueryRsp.HbaseKV kv : list) {
            System.out.println("kv: " + kv.getRowkey() + ", " + kv.getValue());
        }
    }

    /**
     * Fetch all hbase tables.
     */
    public HbaseInfoRsp fetchHbaseTables() throws IOException {
        HbaseInfoRsp rsp = new HbaseInfoRsp();
        Admin admin = HbaseClient.getAdmin();
        try {
            HTableDescriptor[] tables = admin.listTables();
            for (HTableDescriptor td : tables) {
                String tName = td.getNameAsString();
                Collection<HColumnDescriptor> cfs = td.getFamilies();
                rsp.addTable(tName, cfs);
            }
            return rsp;
        } finally {
            if (admin != null) {
                admin.close();
            }
        }
    }

    /**
     * Fetch the table structure of the given table.
     */
    public HbaseInfoRsp fetchHTableInfo(String tableName) throws IOException {
        HbaseInfoRsp rsp = new HbaseInfoRsp();
        Admin admin = HbaseClient.getAdmin();
        try {
            HTableDescriptor td = admin.getTableDescriptor(TableName.valueOf(tableName));
            rsp.addTable(tableName, td.getFamilies());
            return rsp;
        } finally {
            if (admin != null) {
                admin.close();
            }
        }
    }

    /**
     * Query for rsp by the given startrow and endrow.
     */
    public HbaseQueryRsp query(String tableName, String startKey, String endKey) throws IOException {
        HbaseQueryRsp rsp = new HbaseQueryRsp(tableName);
        try {
            queryForRsp(tableName, startKey, endKey, rsp);
        } catch (IOException e) {
            LOG.error("Error while scanning: ", e);
            throw e;
        }
        return rsp;
    }

    private void queryForRsp(String tableName, String startKey, String endKey, HbaseQueryRsp rsp)
            throws IOException {
        ScanConstructor sc = new ScanConstructor(startKey, endKey, tableName);
        Scan scan = sc.getScan();
        Table htable = HbaseClient.getHTable(tableName);
        ResultScanner rss = null;
        int count = 0;
        try {
            rss = htable.getScanner(scan);
            for (Result rs : rss) {
                for (Cell cell : rs.rawCells()) {
                    count++;
                    rsp.addKV(CellUtil.cloneRow(cell), CellUtil.cloneFamily(cell),
                            CellUtil.cloneQualifier(cell), CellUtil.cloneValue(cell));
                    if (count >= RSP_THREASHOLD) {
                        break;// reach maximum rsponse number
                    }
                }
                if (count >= RSP_THREASHOLD) {
                    break;// reach maximum rsponse number
                }
            }
        } catch (Exception e) {
            LOG.error("Get scanner error: ", e);
            throw new RuntimeException(e.getMessage(), e);
        } finally {
            if (rss != null) {
                rss.close();
            }
            if (htable != null) {
                htable.close();
            }
        }
    }

    /**
     * Query for shell executing result by the given shell.
     */
    public HbaseShellRsp queryByShell(HbaseShellEntity shell) {
        HbaseShellRsp rsp = new HbaseShellRsp();
        HbaseShellExecutor executor = new HbaseShellExecutor();
        try {
            if (executor.execute(shell.getShellStr(), rsp)) {
                LOG.info("Execute shell succeeded: [{}]", shell);
                return rsp;
            }
            LOG.error("Execute shell failed: [{}], failed cause: [{}]", shell, rsp.getThrowable());
            return rsp;
        } finally {
            executor.close();
        }
    }
}
