package com.chinasofti.ark.bdadp.util.dto;

import java.util.Date;

/**
 * @Author : water
 * @Date : 2016年9月11日
 * @Desc : 任务调度网络传输对象
 * @version: V1.0
 */
public class MonitorDTO {

    private String userId;
    private String userName;
    private String executionId;
    private String scenarioName;
    private String taskId;
    private String scenarioId;
    private int intStatus;
    private String status;
    private Date dateCommitTime;
    private Date dateCreateTime;
    private String commitTime;
    private Date endTime;
    private String runTime;
    private double progress;
    private String taskName;

    public MonitorDTO(String taskName, String executionId, String taskId, String scenarioId,
                      int intStatus,
                      Date dateCommitTime, Date endTime, double progress, Date dateCreateTime) {
        super();
        this.taskName = taskName;
        this.executionId = executionId;
        this.taskId = taskId;
        this.scenarioId = scenarioId;
        this.intStatus = intStatus;
        this.dateCommitTime = dateCommitTime;
        this.endTime = endTime;
        this.progress = progress;
        this.dateCreateTime = dateCreateTime;
    }

    public Date getDateCreateTime() {
        return dateCreateTime;
    }

    public void setDateCreateTime(Date dateCreateTime) {
        this.dateCreateTime = dateCreateTime;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getExecutionId() {
        return executionId;
    }

    public void setExecutionId(String executionId) {
        this.executionId = executionId;
    }

    public String getScenarioName() {
        return scenarioName;
    }

    public void setScenarioName(String scenarioName) {
        this.scenarioName = scenarioName;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getScenarioId() {
        return scenarioId;
    }

    public void setScenarioId(String scenarioId) {
        this.scenarioId = scenarioId;
    }

    public int getIntStatus() {
        return intStatus;
    }

    public void setIntStatus(int intStatus) {
        this.intStatus = intStatus;
    }

    public Date getDateCommitTime() {
        return dateCommitTime;
    }

    public void setDateCommitTime(Date dateCommitTime) {
        this.dateCommitTime = dateCommitTime;
    }

    public String getCommitTime() {
        return commitTime;
    }

    public void setCommitTime(String commitTime) {
        this.commitTime = commitTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getRunTime() {
        return runTime;
    }

    public void setRunTime(String runTime) {
        this.runTime = runTime;
    }

    public double getProgress() {
        return progress;
    }

    public void setProgress(double progress) {
        this.progress = progress;
    }
}
