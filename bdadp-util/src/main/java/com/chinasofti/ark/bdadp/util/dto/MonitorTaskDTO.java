package com.chinasofti.ark.bdadp.util.dto;

import java.util.Date;

/**
 * @Author : water
 * @Date : 2016年9月11日
 * @Desc : 任务调度网络传输对象
 * @version: V1.0
 */
public class MonitorTaskDTO {

    private String taskName;

    private String status;

    private String commitTime;

    private String runTime;


    private String executionId;

    private String taskId;

    private String scenarioId;

    private int intStatus;

    private Date dateCommitTime;

    private Date endTime;

    private int progress;


    public MonitorTaskDTO(String executionId, String taskId, String scenarioId, int intStatus,
                          Date dateCommitTime, Date endTime) {
        super();
        this.executionId = executionId;
        this.taskId = taskId;
        this.scenarioId = scenarioId;
        this.intStatus = intStatus;
        this.dateCommitTime = dateCommitTime;
        this.endTime = endTime;
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


    public String getExecutionId() {
        return executionId;
    }

    public void setExecutionId(String executionId) {
        this.executionId = executionId;
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

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }


}
