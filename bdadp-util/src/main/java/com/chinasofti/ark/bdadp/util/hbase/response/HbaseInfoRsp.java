package com.chinasofti.ark.bdadp.util.hbase.response;

import com.chinasofti.ark.bdadp.util.hbase.common.HTableDescriptor;
import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.hadoop.hbase.HColumnDescriptor;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Rsp back to client, wrapped up with HTables and descriptor.
 */
public class HbaseInfoRsp {

    private Map<String, HTableDescriptor> tables = Maps.newHashMap();

    public void addTable(String tableName, Collection<HColumnDescriptor> cfs) {
        HTableDescriptor htd = new HTableDescriptor(tableName);
        List<String>
                cfString =
                Lists.transform(Lists.newArrayList(cfs), new Function<HColumnDescriptor, String>() {

                    @Override
                    public String apply(HColumnDescriptor input) {
                        return input.getNameAsString();
                    }
                });
        htd.setCfs(cfString);
        tables.put(tableName, htd);
    }

    public Map<String, HTableDescriptor> getTables() {
        return tables;
    }

    public void setTables(Map<String, HTableDescriptor> tables) {
        this.tables = tables;
    }

    @Override
    public String toString() {
        return tables.toString();
    }
}
