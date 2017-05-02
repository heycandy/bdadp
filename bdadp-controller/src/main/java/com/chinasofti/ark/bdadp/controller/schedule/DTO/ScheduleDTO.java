package com.chinasofti.ark.bdadp.controller.schedule.DTO;

import java.util.Date;


/**
 * @Author : water
 * @Date : 2016年9月9日
 * @Desc : TODO
 * @version: V1.0
 */
public class ScheduleDTO {

    // 场景id
    private String scenarioId;

    // 关联集群id
    private String clusterId;

    // 前置场景id
    private String preScenarioId;

    private String scheduleId;

    private Date startTime;

    private Date endTime;

    // 执行周期
    private int executionCycle;

    // 执行时间（小时）
    private int executionTime;

    // corn表达式
    private String cornExpression;

    /**
     * 任务调度状态（0就绪,1运行中,2成功,3失败，4外部中断）
     */
    private int scheduleStatus;

    public String getScenarioId() {
        return scenarioId;
    }

    public void setScenarioId(String scenarioId) {
        this.scenarioId = scenarioId;
    }

    public String getClusterId() {
        return clusterId;
    }

    public void setClusterId(String clusterId) {
        this.clusterId = clusterId;
    }

    public String getPreScenarioId() {
        return preScenarioId;
    }

    public void setPreScenarioId(String preScenarioId) {
        this.preScenarioId = preScenarioId;
    }

    public String getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(String scheduleId) {
        this.scheduleId = scheduleId;
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

    public int getExecutionCycle() {
        return executionCycle;
    }

    public void setExecutionCycle(int executionCycle) {
        this.executionCycle = executionCycle;
    }

    public int getExecutionTime() {
        return executionTime;
    }

    public void setExecutionTime(int executionTime) {
        this.executionTime = executionTime;
    }

    public String getCornExpression() {
        return cornExpression;
    }

    public void setCornExpression(String cornExpression) {
        this.cornExpression = cornExpression;
    }

    public int getScheduleStatus() {
        return scheduleStatus;
    }

    public void setScheduleStatus(int scheduleStatus) {
        this.scheduleStatus = scheduleStatus;
    }


}
