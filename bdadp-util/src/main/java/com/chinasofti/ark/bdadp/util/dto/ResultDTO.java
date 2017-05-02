package com.chinasofti.ark.bdadp.util.dto;


/**
 * @Author : water
 * @Date : 2016年9月11日
 * @Desc : 运行结果网络传输对象
 * @version: V1.0
 */

public class ResultDTO {

    private int resultCode;

    private Object resultMessage;

    private Object cause;

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    public Object getResultMessage() {
        return resultMessage;
    }

    public void setResultMessage(Object resultMessage) {
        this.resultMessage = resultMessage;
    }

    public Object getCause() {
        return cause;
    }

    public void setCause(Object cause) {
        this.cause = cause;
    }


}
