package com.chinasofti.ark.bdadp.entity.user;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

/**
 * Created by White on 2016/08/31.
 */
@Entity
public class Scope {

    @Id
    private String scopeId;
    private String scopeName;
    private String scopeDesc;
    private String scopeType;
    private String scopeValue;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    private String createUser;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date modifiedTime;

    private String modifiedUser;

    public String getScopeId() {
        return scopeId;
    }

    public void setScopeId(String scopeId) {
        this.scopeId = scopeId;
    }

    public String getScopeName() {
        return scopeName;
    }

    public void setScopeName(String scopeName) {
        this.scopeName = scopeName;
    }

    public String getScopeDesc() {
        return scopeDesc;
    }

    public void setScopeDesc(String scopeDesc) {
        this.scopeDesc = scopeDesc;
    }

    public String getScopeType() {
        return scopeType;
    }

    public void setScopeType(String scopeType) {
        this.scopeType = scopeType;
    }

    public String getScopeValue() {
        return scopeValue;
    }

    public void setScopeValue(String scopeValue) {
        this.scopeValue = scopeValue;
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

    @Override
    public String toString() {
        return "Scope{" +
                "scopeId='" + scopeId + '\'' +
                ", scopeName='" + scopeName + '\'' +
                ", scopeDesc='" + scopeDesc + '\'' +
                ", scopeType='" + scopeType + '\'' +
                ", scopeValue='" + scopeValue + '\'' +
                ", createTime=" + createTime +
                ", createUser=" + createUser +
                ", modifiedTime=" + modifiedTime +
                ", modifiedUser=" + modifiedUser +
                '}';
    }
}
