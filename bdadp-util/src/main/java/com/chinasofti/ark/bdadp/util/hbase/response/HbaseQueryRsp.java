package com.chinasofti.ark.bdadp.util.hbase.response;

import com.chinasofti.ark.bdadp.util.common.StringUtils;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import org.apache.hadoop.hbase.util.Bytes;

import java.util.List;

/**
 * Rsp wrapped up with query results.
 */
public class HbaseQueryRsp {

    private String tableName;

    private List<HbaseKV> kvs = Lists.newArrayList();

    public HbaseQueryRsp(String tabName) {
        Preconditions.checkArgument(!StringUtils.isNulOrEmpty(tabName), "table name is null or empty.");
        this.tableName = tabName;
    }

    public String getTableName() {
        return tableName;
    }

    public List<HbaseKV> getKvs() {
        return kvs;
    }

    public void addKV(HbaseKV kv) {
        this.kvs.add(kv);
    }

    public void addKV(byte[] rk, byte[] cf, byte[] qualifier, byte[] value) {
        HbaseKV kv = new HbaseKV(Bytes.toString(rk));
        kv.setValue(cf, qualifier, Bytes.toString(value));
        this.kvs.add(kv);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("TableName: [").append(this.tableName).append("] \t")
                .append("All values: ").append(this.kvs);
        return sb.toString();
    }

    public static class HbaseKV {

        private String rowkey;

        private String cf;

        private String qualifier;

        private String value;

        public HbaseKV(String rowkey) {
            Preconditions.checkArgument(!StringUtils.isNulOrEmpty(rowkey), "rowkey is null or empty.");
            this.rowkey = rowkey;
        }

        public String getRowkey() {
            return rowkey;
        }

        public String getValue() {
            return value;
        }

        public String getCf() {
            return cf;
        }

        public String getQualifier() {
            return qualifier;
        }

        public void setValue(byte[] cf, byte[] qualifier, String value) {
            String cfStr = Bytes.toString(cf);
            String qualifierStr = Bytes.toString(qualifier);
            this.cf = cfStr;
            this.qualifier = qualifierStr;
            this.value = value;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("rowkey = ").append(this.rowkey).append(" ")
                    .append("cf = ").append(this.cf).append(" ")
                    .append("qualifier = ").append(this.qualifier).append(" ")
                    .append("value = ").append(this.value);
            return sb.toString();
        }
    }

}
