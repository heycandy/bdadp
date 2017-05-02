package com.chinasofti.ark.bdadp.util.hbase.common;

import com.chinasofti.ark.bdadp.util.common.StringUtils;
import com.google.common.base.Preconditions;

import java.util.List;

/**
 * Descriptor of hbase table, including rowkey and cf.
 */
public class HTableDescriptor {

    private String tableName;

    private List<String> cfs;

    public HTableDescriptor(String tabName) {
        Preconditions.checkArgument(!StringUtils.isNulOrEmpty(tabName), "table name is null or empty.");
        this.tableName = tabName;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public List<String> getCfs() {
        return cfs;
    }

    public void setCfs(List<String> cfs) {
        this.cfs = cfs;
    }

    @Override
    public String toString() {
        return this.cfs.toString();
    }
}
