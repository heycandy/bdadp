package com.chinasofti.ark.bdadp.util.hbase.common;

import com.chinasofti.ark.bdadp.util.common.StringUtils;
import com.google.common.base.Preconditions;

/**
 * Input of hbase shell execuations.
 */
public class HbaseShellEntity {

    private String shellStr;

    public HbaseShellEntity() {

    }

    public HbaseShellEntity(String shellString) {
        Preconditions.checkArgument(StringUtils.isNulOrEmpty(shellString), "Input is null.");
        this.shellStr = shellString;
    }

    public String getShellStr() {
        return shellStr;
    }

    public void setShellStr(String shellStr) {
        this.shellStr = shellStr;
    }

    @Override
    public String toString() {
        return this.shellStr;
    }
}
