package com.chinasofti.ark.bdadp.util.hbase.common;

import com.chinasofti.ark.bdadp.util.common.StringUtils;
import com.google.common.base.Preconditions;

/**
 * Parser used to parse Hbase Shell strings.
 */
public class HShellStringParser {

    public static final String SHELL_SEP = " ";

    public static final String PARA_SEP = ":";

    private String[] parser;

    private String sep;

    public HShellStringParser(String str, String seperator) {
        Preconditions.checkArgument(!StringUtils.isNulOrEmpty(str), "input string is null.");
        Preconditions.checkArgument(!StringUtils.isNulOrEmpty(seperator), "seperator is null.");
        this.sep = seperator;
        parseString(str);
    }

    private void parseString(String str) {
        parser = str.split(sep);
    }

    public String getHeader() {
        return this.parser[0];
    }

    public String getOpType() {
        return this.parser[1];
    }

    public String getOperation() {
        String rawOp = this.parser[2];
        if (rawOp.startsWith("-")) {
            String op = rawOp.substring(1);
            return op;
        }
        throw new RuntimeException("Operaton should start with '-'. ");
    }

    public String getOpParameters() {
        return this.parser[3];
    }

}
