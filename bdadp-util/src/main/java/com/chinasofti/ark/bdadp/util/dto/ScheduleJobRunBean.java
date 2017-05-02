package com.chinasofti.ark.bdadp.util.dto;

/**
 * @Author : water
 * @Date : 2016年9月11日
 * @Desc : TODO
 * @version: V1.0
 */
public class ScheduleJobRunBean {

    private String scenarioId;

    private String userId;

    /**
     * 任务id
     */
    private String jobGroup;

    public String getScenarioId() {
        return scenarioId;
    }

    public void setScenarioId(String scenarioId) {
        this.scenarioId = scenarioId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getJobGroup() {
        return jobGroup;
    }

    public void setJobGroup(String jobGroup) {
        this.jobGroup = jobGroup;
    }

}
