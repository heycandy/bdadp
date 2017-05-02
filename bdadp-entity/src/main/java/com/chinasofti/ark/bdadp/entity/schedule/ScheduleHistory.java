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
public class ScheduleHistory implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "schedulehistory_id")
    @JsonProperty("schedulehistory_id")
    private String schedulehistoryId;

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

    @Column(name = "schedule_status")
    @JsonProperty("schedule_status")
    private String scheduleStatus;

    public String getSchedulehistoryId() {
        return schedulehistoryId;
    }

    public void setSchedulehistoryId(String schedulehistoryId) {
        this.schedulehistoryId = schedulehistoryId;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
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

    public String getScheduleStatus() {
        return scheduleStatus;
    }

    public void setScheduleStatus(String scheduleStatus) {
        this.scheduleStatus = scheduleStatus;
    }

}
