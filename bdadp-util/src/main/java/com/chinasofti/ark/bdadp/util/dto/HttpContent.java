package com.chinasofti.ark.bdadp.util.dto;


/**
 * @Author : water
 * @Date : 2016年9月20日
 * @Desc : TODO
 * @version: V1.0
 */
public class HttpContent {

    private int statusCode;

    private String responseBody;

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getResponseBody() {
        return responseBody;
    }

    public void setResponseBody(String responseBody) {
        this.responseBody = responseBody;
    }


}
