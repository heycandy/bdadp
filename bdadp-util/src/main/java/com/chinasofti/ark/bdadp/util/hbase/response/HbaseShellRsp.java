package com.chinasofti.ark.bdadp.util.hbase.response;

import com.chinasofti.ark.bdadp.util.common.StringUtils;

/**
 * Rsp of shell execuations.
 */
public class HbaseShellRsp {

    private static final String SEP_LINE = "</br>"; // line seperator
    private StringBuilder message = new StringBuilder();
    // used to append result info.; // to record exceptions.
    private StringBuilder throwable = new StringBuilder();

    public StringBuilder getMessage() {
        return message;
    }

    public StringBuilder getThrowable() {
        return throwable;
    }

    /**
     * Append result info to rsp.
     */
    public void appendResult(String info) {
        if (!StringUtils.isNulOrEmpty(info)) {
            this.message.append(info).append(SEP_LINE);
        }
    }

    /**
     * Record error.
     */
    public void recordError(Throwable error) {
        this.throwable.append(error.getMessage().toString()).append(SEP_LINE);
    }

    /**
     * Check if the result has error.
     */
    public boolean hasError() {
        return this.throwable != null;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("message: ").append(this.message).append("\t");
        if (!this.throwable.toString().isEmpty()) {
            sb.append("error: ").append(this.throwable);
        }
        return sb.toString();
    }

}
