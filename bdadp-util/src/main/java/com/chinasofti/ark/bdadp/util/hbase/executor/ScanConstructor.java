package com.chinasofti.ark.bdadp.util.hbase.executor;

import com.chinasofti.ark.bdadp.util.hbase.common.HbaseClient;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.util.Pair;

import java.io.IOException;

/**
 * To construct scans, including rk operations and filter operations.
 */
public class ScanConstructor {

    private Pair<String, String> rk;

    private Scan scan;

    private HColumnDescriptor[] hcds;

    public ScanConstructor(String startKey, String endKey, String tableName) {
//		Preconditions.checkArgument(!StringUtils.isNulOrEmpty(startKey), "startkey is null");
        rk = new Pair<String, String>(startKey, endKey);
        try {
            initScanProps(startKey, endKey, tableName);
        } catch (IOException e) {
            throw new RuntimeException("Scan init error: ", e);
        }
    }

    public Scan getScan() {
        return scan;
    }

    private void initScanProps(String startKey, String endKey, String tableName) throws IOException {
        scan = new Scan(startKey.getBytes());
        scan.setStopRow(endKey.getBytes());
        //TODO:properties setting
        scan.setCaching(100);
        Admin admin = HbaseClient.getAdmin();
        try {
            HTableDescriptor des = admin.getTableDescriptor(TableName.valueOf(tableName));
            hcds = des.getColumnFamilies();
            for (HColumnDescriptor hc : hcds) {
                scan.addFamily(hc.getName());
            }
        } finally {
            admin.close();
        }
    }

    public void addColumn(byte[] cf, byte[] qualifier) {
        scan.addColumn(cf, qualifier);
    }

    public HColumnDescriptor[] getHcds() {
        return hcds;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("rowkey: ").append(this.rk);
        return sb.toString();
    }
}
