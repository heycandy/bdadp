package com.chinasofti.ark.bdadp.controller.bean;

/**
 * Created by White on 2016/08/26.
 */
public class ResultBody<T> {

    private int resultCode;
    private String resultMessage;
    private Object[] resultArgs;
    private T result;

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    public String getResultMessage() {
        return resultMessage;
    }

    public void setResultMessage(String resultMessage) {
        this.resultMessage = resultMessage;
    }

    public Object[] getResultArgs() {
        return resultArgs;
    }

    public void setResultArgs(Object[] resultArgs) {
        this.resultArgs = resultArgs;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

}
