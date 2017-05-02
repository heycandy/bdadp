package com.chinasofti.ark.bdadp.entity.schedule;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import java.io.Serializable;
import java.util.Date;

/**
 * @Author : water
 * @Date : 2016年9月9日
 * @Desc : 任务调度实体
 * @version: V1.0
 */
@Entity
public class ScenarioExecuteHistory implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private ScenarioExecuteHistoryPK unionId;

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

    @Column(name = "create_time")
    @JsonProperty("create_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
    /**
     * 任务调度状态（0就绪,1运行中,2成功,3失败，4外部中断）
     */
    @Column(name = "execute_status")
    @JsonProperty("execute_status")
    private int executeStatus;
    @Column(name = "execute_progress")
    @JsonProperty("execute_progress")
    private double executeProgress;
    @Column(name = "task_name")
    @JsonProperty("task_name")
    private String taskName;

    public ScenarioExecuteHistory() {
    }

    public ScenarioExecuteHistory(String executionId, String taskId) {
        this.unionId = new ScenarioExecuteHistoryPK(executionId, taskId);
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public ScenarioExecuteHistoryPK getUnionId() {
        return unionId;
    }

    public void setUnionId(ScenarioExecuteHistoryPK unionId) {
        this.unionId = unionId;
    }

    public String getExecutionId() {
        return unionId.getExecutionId();
    }

    public String getScenarioId() {
        return scenarioId;
    }

    public void setScenarioId(String scenarioId) {
        this.scenarioId = scenarioId;
    }

    public String getTaskId() {
        return unionId.getTaskId();
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

    public int getExecuteStatus() {
        return executeStatus;
    }

    public void setExecuteStatus(int executeStatus) {
        this.executeStatus = executeStatus;
    }

    public double getExecuteProgress() {
        return executeProgress;
    }

    public void setExecuteProgress(double executeProgress) {
        this.executeProgress = executeProgress;
    }
}
