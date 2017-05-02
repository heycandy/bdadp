package com.chinasofti.ark.bdadp.entity.schedule;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

/**
 * @Author : water
 * @Date : 2016年9月9日
 * @Desc : 任务调度实体
 * @version: V1.0
 */
@Entity
public class Schedule implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "scenario_id")
    @JsonProperty("scenario_id")
    private String scenarioId;

    @Column(name = "start_time")
    @JsonProperty("start_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date startTime;

    @Column(name = "end_time")
    @JsonProperty("end_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date endTime;

    @Column(name = "repeat_count")
    @JsonProperty("repeat_count")
    private int repeatCount;

    @Column(name = "create_time")
    @JsonProperty("create_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @Column(name = "repeat_interval")
    @JsonProperty("repeat_interval")
    private int repeatInterval;

    @Column(name = "execution_frequency_unit")
    @JsonProperty("execution_frequency_unit")
    private String executionFrequencyUnit;

    @Column(name = "execution_day")
    @JsonProperty("execution_day")
    private String executionDay;

    @Column(name = "execution_time")
    @JsonProperty("execution_time")
    private String executionTime;

    @Column(name = "cron_expression")
    @JsonProperty("cron_expression")
    private String cronExpression;

    @Column(name = "trigger_type")
    @JsonProperty("trigger_type")
    private String triggerType;

    @Column(name = "job_group")
    @JsonProperty("job_group")
    private String jobGroup;

    @Column(name = "user_id")
    @JsonProperty("user_id")
    private String userId;

    @Column(name = "ip_address")
    @JsonProperty("ip_address")
    private String ipAddress;

    public String getExecutionDay() {
        return executionDay;
    }

    public void setExecutionDay(String executionDay) {
        this.executionDay = executionDay;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
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

    public String getScenarioId() {
        return scenarioId;
    }

    public void setScenarioId(String scenarioId) {
        this.scenarioId = scenarioId;
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

    public int getRepeatCount() {
        return repeatCount;
    }

    public void setRepeatCount(int repeatCount) {
        this.repeatCount = repeatCount;
    }

    public int getRepeatInterval() {
        return repeatInterval;
    }

    public void setRepeatInterval(int repeatInterval) {
        this.repeatInterval = repeatInterval;
    }

    public String getCronExpression() {
        return cronExpression;
    }

    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
    }

    public String getTriggerType() {
        return triggerType;
    }

    public void setTriggerType(String triggerType) {
        this.triggerType = triggerType;
    }

    public String getJobGroup() {
        return jobGroup;
    }

    public void setJobGroup(String jobGroup) {
        this.jobGroup = jobGroup;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

}
