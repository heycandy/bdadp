package com.chinasofti.ark.bdadp.service.task;

/**
 * Created by White on 2016/10/13.
 */
public class TaskServiceException extends RuntimeException {

    private final String PREFIX = "TSE-";

    private int resultCode = 1;
    private String resultMessage;

    public TaskServiceException() {
        this(23999, "Task service error.");
    }

    public TaskServiceException(int resultCode, String message) {
        super(message);

        setResultCode(resultCode);
    }

    public TaskServiceException(int resultCode, String message, Throwable cause) {
        super(message, cause);

        this.resultCode = resultCode;
        this.resultMessage = PREFIX + resultCode;
    }

    public TaskServiceException(int resultCode, Throwable cause) {
        super(cause);

        setResultCode(resultCode);
    }

    public TaskServiceException(int resultCode, String message, Throwable cause,
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

}
