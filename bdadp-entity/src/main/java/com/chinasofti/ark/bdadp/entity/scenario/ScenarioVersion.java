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
public class ScenarioVersion implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    //Version ID
    //顶点 ID
    @Id
    @Column(name = "version_id")
    @JsonProperty("version_id")
    private String versionId;
    //Version Lable
    @Column(name = "version_label")
    @JsonProperty("version_label")
    private String versionLabel;
    //创建时间
    @Column(name = "create_time")
    @JsonProperty("create_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
    //创建人
    @Column(name = "create_user")
    @JsonProperty("create_user")
    private String createUser;
    //场景ID
    @Column(name = "scenario_id")
    @JsonProperty("scenario_id")
    private String scenarioId;

    public ScenarioVersion() {
    }

    public ScenarioVersion(String versionId, String versionLabel, Date createTime,
                           String createUser, String scenarioId) {
        this.versionId = versionId;
        this.versionLabel = versionLabel;
        this.createTime = createTime;
        this.createUser = createUser;
        this.scenarioId = scenarioId;
    }

    public String getVersionId() {
        return versionId;
    }

    public void setVersionId(String versionId) {
        this.versionId = versionId;
    }

    public String getVersionLabel() {
        return versionLabel;
    }

    public void setVersionLabel(String versionLabel) {
        this.versionLabel = versionLabel;
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

    public String getScenarioId() {
        return scenarioId;
    }

    public void setScenarioId(String scenarioId) {
        this.scenarioId = scenarioId;
    }

    @Override
    public String toString() {
        return "ScenarioVersion{" +
                "versionId='" + versionId + '\'' +
                ", versionLabel='" + versionLabel + '\'' +
                ", createTime=" + createTime +
                ", createUser='" + createUser + '\'' +
                ", scenarioId='" + scenarioId + '\'' +
                '}';
    }
}
