package com.chinasofti.ark.bdadp.entity.scenario;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

/**
 * Created by zhangweigang on 2016/08/27.
 */
@Entity
public class Scenario implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "scenario_id")
    @JsonProperty("scenario_id")
    private String scenarioId;

    //场景名称
    @Column(name = "scenario_name")
    @JsonProperty("scenario_name")
    private String scenarioName;

    //场景描述
    @Column(name = "scenario_desc")
    @JsonProperty("scenario_desc")
    private String scenarioDesc;

    //场景附加属性 for labo
    @Column(name = "scenario_extra")
    @JsonProperty("scenario_extra")
    private String scenarioExtra;

    //场景附加属性 for fujun
    @Column(name = "scenario_col")
    @JsonProperty("scenario_col")
    private String scenarioCol;

    /**
     * 场景状态 0：就绪中 1：已就绪 2：已检视 3：已上线 4：已下线 5：已删除 6：已出错
     */
    @Column(name = "scenario_status")
    @JsonProperty("scenario_status")
    private int scenarioStatus;

    //创建时间
    @Column(name = "create_time")
    @JsonProperty("create_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    //创建操作人
    @Column(name = "create_user")
    @JsonProperty("create_user")
    private String createUser;

    //修改时间
    @Column(name = "modified_time")
    @JsonProperty("modified_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date modifiedTime;

    //修改操作人
    @Column(name = "modified_user")
    @JsonProperty("modified_user")
    private String modifiedUser;

    //核检时间
    @Column(name = "inspect_time")
    @JsonProperty("inspect_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date inspectTime;

    //核检操作人
    @Column(name = "inspect_user")
    @JsonProperty("inspect_user")
    private String inspectUser;

    //上线时间
    @Column(name = "online_time")
    @JsonProperty("online_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date onlineTime;

    //上线操作人
    @Column(name = "online_user")
    @JsonProperty("online_user")
    private String onlineUser;

    //下线时间
    @Column(name = "offline_time")
    @JsonProperty("offline_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date offlineTime;

    //下线操作人
    @Column(name = "offline_user")
    @JsonProperty("offline_user")
    private String offlineUser;

    public String getScenarioId() {
        return scenarioId;
    }

    public void setScenarioId(String scenarioId) {
        this.scenarioId = scenarioId;
    }

    public String getScenarioName() {
        return scenarioName;
    }

    public void setScenarioName(String scenarioName) {
        this.scenarioName = scenarioName;
    }

    public String getScenarioDesc() {
        return scenarioDesc;
    }

    public void setScenarioDesc(String scenarioDesc) {
        this.scenarioDesc = scenarioDesc;
    }

    public String getScenarioExtra() {
        return scenarioExtra;
    }

    public void setScenarioExtra(String scenarioExtra) {
        this.scenarioExtra = scenarioExtra;
    }

    public String getScenarioCol() {
        return scenarioCol;
    }

    public void setScenarioCol(String scenarioCol) {
        this.scenarioCol = scenarioCol;
    }

    public int getScenarioStatus() {
        return scenarioStatus;
    }

    public void setScenarioStatus(int scenarioStatus) {
        this.scenarioStatus = scenarioStatus;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public Date getModifiedTime() {
        return modifiedTime;
    }

    public void setModifiedTime(Date modifiedTime) {
        this.modifiedTime = modifiedTime;
    }

    public String getModifiedUser() {
        return modifiedUser;
    }

    public void setModifiedUser(String modifiedUser) {
        this.modifiedUser = modifiedUser;
    }

    public Date getInspectTime() {
        return inspectTime;
    }

    public void setInspectTime(Date inspectTime) {
        this.inspectTime = inspectTime;
    }

    public String getInspectUser() {
        return inspectUser;
    }

    public void setInspectUser(String inspectUser) {
        this.inspectUser = inspectUser;
    }

    public Date getOnlineTime() {
        return onlineTime;
    }

    public void setOnlineTime(Date onlineTime) {
        this.onlineTime = onlineTime;
    }

    public String getOnlineUser() {
        return onlineUser;
    }

    public void setOnlineUser(String onlineUser) {
        this.onlineUser = onlineUser;
    }

    public Date getOfflineTime() {
        return offlineTime;
    }

    public void setOfflineTime(Date offlineTime) {
        this.offlineTime = offlineTime;
    }

    public String getOfflineUser() {
        return offlineUser;
    }

    public void setOfflineUser(String offlineUser) {
        this.offlineUser = offlineUser;
    }

    @Override
    public String toString() {
        return "Scenario{" +
                "scenarioId='" + scenarioId + '\'' +
                ", scenarioName='" + scenarioName + '\'' +
                ", scenarioDesc='" + scenarioDesc + '\'' +
                ", scenarioExtra='" + scenarioExtra + '\'' +
                ", scenarioCol='" + scenarioCol + '\'' +
                ", scenarioStatus=" + scenarioStatus +
                ", createTime=" + createTime +
                ", createUser='" + createUser + '\'' +
                ", modifiedTime=" + modifiedTime +
                ", modifiedUser='" + modifiedUser + '\'' +
                ", inspectTime=" + inspectTime +
                ", inspectUser='" + inspectUser + '\'' +
                ", onlineTime=" + onlineTime +
                ", onlineUser='" + onlineUser + '\'' +
                ", offlineTime=" + offlineTime +
                ", offlineUser='" + offlineUser + '\'' +
                '}';
    }
}
