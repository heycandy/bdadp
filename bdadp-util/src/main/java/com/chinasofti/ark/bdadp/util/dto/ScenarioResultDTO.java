package com.chinasofti.ark.bdadp.util.dto;


/**
 * @Author : water
 * @Date : 2016年9月11日
 * @Desc : 运行结果网络传输对象
 * @version: V1.0
 */

public class ScenarioResultDTO {

    private int resultCode;

    private String resultMessage;

    private String result;

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

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }


}
