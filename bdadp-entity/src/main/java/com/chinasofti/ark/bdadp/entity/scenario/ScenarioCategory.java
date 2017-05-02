package com.chinasofti.ark.bdadp.entity.scenario;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.Date;
import java.util.List;


/**
 * Created by White on 2016/08/27.
 */

@Entity
public class ScenarioCategory implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "cate_id")
    @JsonProperty("cate_id")
    private String cateId;

    @Column(name = "cate_name")
    @JsonProperty("cate_name")
    private String cateName;

    @Column(name = "cate_desc")
    @JsonProperty("cate_desc")
    private String cateDesc;

    @Column(name = "cate_status")
    @JsonProperty("cate_status")
    private int cateStatus;

    @Column(name = "create_time")
    @JsonProperty("create_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @Column(name = "create_user")
    @JsonProperty("create_user")
    private String createUser;

    @Column(name = "modified_time")
    @JsonProperty("modified_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date modifiedTime;

    @Column(name = "modified_user")
    @JsonProperty("modified_user")
    private String modifiedUser;

    @OneToMany(fetch = FetchType.EAGER)
    @Fetch(FetchMode.SUBSELECT)
    @JoinColumn(name = "cate_id")
    @OrderBy(value = "createTime")
    @JsonProperty("details")
    private List<ScenarioCategoryDetail> details;

    public String getCateId() {
        return cateId;
    }

    public void setCateId(String cateId) {
        this.cateId = cateId;
    }

    public String getCateName() {
        return cateName;
    }

    public void setCateName(String cateName) {
        this.cateName = cateName;
    }

    public String getCateDesc() {
        return cateDesc;
    }

    public void setCateDesc(String cateDesc) {
        this.cateDesc = cateDesc;
    }

    public int getCateStatus() {
        return cateStatus;
    }

    public void setCateStatus(int cateStatus) {
        this.cateStatus = cateStatus;
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

    public List<ScenarioCategoryDetail> getDetails() {
        return details;
    }

    public void setDetails(List<ScenarioCategoryDetail> details) {
        this.details = details;
    }
}
