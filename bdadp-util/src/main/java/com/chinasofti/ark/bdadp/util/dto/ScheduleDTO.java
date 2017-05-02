package com.chinasofti.ark.bdadp.util.dto;

import java.util.Date;
import java.util.Map;

/**
 * @Author : water
 * @Date : 2016年9月11日
 * @Desc : 任务调度网络传输对象
 * @version: V1.0
 */
public class ScheduleDTO {

    private String userId;

    private String userName;

    private String executionId;

    private String scenarioName;

    private String taskId;

    private String triggerType;

    private String scenarioId;

    private String jobGroup;

    private double progress;

    private Map<String, Object> jobDataMap;

    private int status;

    private String cronExpression;

    private Date startTime;

    private Date endTime;

    private String startTimeStr;

    private String endTimeStr;

    private int repeatInterval;

    private String executionFrequencyUnit;

    private String executionDay;
    private String executionTime;

    public ScheduleDTO() {
        super();
    }

    public ScheduleDTO(String executionId, String taskId, String scenarioId, int status,
                       Date startTime, Date endTime, double progress) {
        this.executionId = executionId;
        this.taskId = taskId;
        this.scenarioId = scenarioId;
        this.status = status;
        this.startTime = startTime;
        this.endTime = endTime;
        this.progress = progress;
    }

    public String getStartTimeStr() {
        return startTimeStr;
    }

    public void setStartTimeStr(String startTimeStr) {
        this.startTimeStr = startTimeStr;
    }

    public String getEndTimeStr() {
        return endTimeStr;
    }

    public void setEndTimeStr(String endTimeStr) {
        this.endTimeStr = endTimeStr;
    }

    public String getExecutionDay() {
        return executionDay;
    }

    public void setExecutionDay(String executionDay) {
        this.executionDay = executionDay;
    }

    public String getExecutionFrequencyUnit() {
        return executionFrequencyUnit;
    }

    public void setExecutionFrequencyUnit(String executionFrequencyUnit) {
        this.executionFrequencyUnit = executionFrequencyUnit;
    }

    public String getExecutionTime() {
        return executionTime;
    }

    public void setExecutionTime(String executionTime) {
        this.executionTime = executionTime;
    }

    public double getProgress() {
        return progress;
    }

    public void setProgress(double progress) {
        this.progress = progress;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getScenarioName() {
        return scenarioName;
    }

    public void setScenarioName(String scenarioName) {
        this.scenarioName = scenarioName;
    }

    public String getTriggerType() {
        return triggerType;
    }

    public void setTriggerType(String triggerType) {
        this.triggerType = triggerType;
    }

    public String getScenarioId() {
        return scenarioId;
    }

    public void setScenarioId(String scenarioId) {
        this.scenarioId = scenarioId;
    }

    public String getJobGroup() {
        return jobGroup;
    }

    public void setJobGroup(String jobGroup) {
        this.jobGroup = jobGroup;
    }


    public Map<String, Object> getJobDataMap() {
        return jobDataMap;
    }

    public void setJobDataMap(Map<String, Object> jobDataMap) {
        this.jobDataMap = jobDataMap;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }


    public String getCronExpression() {
        return cronExpression;
    }

    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }


    public int getRepeatInterval() {
        return repeatInterval;
    }

    public void setRepeatInterval(int repeatInterval) {
        this.repeatInterval = repeatInterval;
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

}
