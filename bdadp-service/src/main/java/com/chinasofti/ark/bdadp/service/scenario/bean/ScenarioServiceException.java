package com.chinasofti.ark.bdadp.service.scenario.bean;

/**
 * Created by White on 2016/10/13.
 */
public class ScenarioServiceException extends RuntimeException {

    private final String PREFIX = "SSE-";

    private int resultCode = 1;
    private String resultMessage;
    private Object[] resultArgs;

    public ScenarioServiceException() {
        this(22999, "Scenario service error.");
    }

    public ScenarioServiceException(int resultCode, String message) {
        super(message);

        setResultCode(resultCode);
    }

    public ScenarioServiceException(int resultCode, String message, Object[] resultArgs) {
        super(message);

        setResultCode(resultCode);
        setResultArgs(resultArgs);
    }

    public ScenarioServiceException(int resultCode, Throwable cause) {
        super(cause);

        setResultCode(resultCode);
    }

    public ScenarioServiceException(int resultCode, String message, Throwable cause) {
        super(message, cause);

        setResultCode(resultCode);
    }

    public ScenarioServiceException(int resultCode, String message, Object[] resultArgs,
                                    Throwable cause) {
        super(message, cause);

        setResultCode(resultCode);
        setResultArgs(resultArgs);
    }

    public ScenarioServiceException(int resultCode, String message, Throwable cause,
                                    boolean enableSuppression,
                                    boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);

        setResultCode(resultCode);
    }

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
        this.resultMessage = PREFIX + resultCode;
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
}
