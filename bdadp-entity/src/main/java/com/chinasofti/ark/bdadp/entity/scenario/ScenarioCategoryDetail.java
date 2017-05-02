package com.chinasofti.ark.bdadp.entity.scenario;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

/**
 * Created by White on 2016/08/27.
 */

@Entity
public class ScenarioCategoryDetail implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "detail_id")
    @JsonProperty("detail_id")
    private String detailId;

    @Column(name = "create_time")
    @JsonProperty("create_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @Column(name = "create_user")
    @JsonProperty("create_user")
    private String createUser;

    @Column(name = "cate_id")
    @JsonProperty("cate_id")
    private String cateId;

    @Column(name = "scenario_id")
    @JsonProperty("scenario_id")
    private String scenarioId;

    public String getDetailId() {
        return detailId;
    }

    public void setDetailId(String detailId) {
        this.detailId = detailId;
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

    public String getCateId() {
        return cateId;
    }

    public void setCateId(String cateId) {
        this.cateId = cateId;
    }

    public String getScenarioId() {
        return scenarioId;
    }

    public void setScenarioId(String scenarioId) {
        this.scenarioId = scenarioId;
    }

    @Override
    public String toString() {
        return "ScenarioCategoryDetail{" +
                "detailId='" + detailId + '\'' +
                ", createTime=" + createTime +
                ", createUser='" + createUser + '\'' +
                ", cateId=" + cateId +
                ", scenarioId='" + scenarioId + '\'' +
                '}';
    }
}
