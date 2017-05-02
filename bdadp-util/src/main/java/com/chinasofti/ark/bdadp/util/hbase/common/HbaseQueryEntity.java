package com.chinasofti.ark.bdadp.util.hbase.common;

/**
 * Entity of hbase query request by rowkey
 */
public class HbaseQueryEntity {

    private String startkey; // startkey
    private String endkey; // endkey
    private String tablename;// target table to query


    public String getStartkey() {
        return startkey;
    }

    public void setStartkey(String startkey) {
        this.startkey = startkey;
    }

    public String getEndkey() {
        return endkey;
    }

    public void setEndkey(String endkey) {
        this.endkey = endkey;
    }

    public String getTablename() {
        return tablename;
    }

    public void setTablename(String tablename) {
        this.tablename = tablename;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(tablename).append("    ")
                .append(startkey).append("    ")
                .append(endkey);
        return sb.toString();
    }
}
