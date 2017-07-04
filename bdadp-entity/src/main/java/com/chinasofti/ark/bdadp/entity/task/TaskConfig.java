package com.chinasofti.ark.bdadp.entity.task;

import com.chinasofti.ark.bdadp.entity.components.ComponentConfig;
//import com.chinasofti.ark.bdadp.util.common.DESUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;

/**
 * Created by TongTong on 2016/08/29.
 */
@Entity
public class TaskConfig implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    //task配置id
    @Id
    @Column(name = "config_id")
    @JsonProperty("config_id")
    private String configId;
    //参数ID
    @Column(name = "param_id")
    @JsonProperty("param_id")
    private String paramId;
    //参数值
    @Column(name = "param_value")
    @JsonProperty("param_value")
    private String paramValue;

    @Column(name = "create_time")
    @JsonProperty("create_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @Column(name = "create_user")
    @JsonProperty("create_user")
    private String createUser;

    //Task ID
    @Column(name = "task_id")
    @JsonProperty("task_id")
    private String taskId;

    @Transient
    @JsonProperty("component_config")
    private ComponentConfig componentConfig;

    public String getConfigId() {
        return configId;
    }

    public void setConfigId(String configId) {
        this.configId = configId;
    }

    public String getParamId() {
        return paramId;
    }

    public void setParamId(String paramId) {
        this.paramId = paramId;
    }

//    public String getParamValue() {
//      if (null != paramValue && !"".equals(paramValue)) {
//        return DESUtil.getDecryptString(paramValue);
//      } else {
//        return "";
//      }
//
//    }
//
//    public void setParamValue(String paramValue) {
//
//      if (null != paramValue && !"".equals(paramValue)) {
//        this.paramValue = DESUtil.getEncryptString(paramValue);
//      } else {
//        this.paramValue = "";
//      }
//    }

    public String getParamValue() {
        return paramValue;
    }

    public void setParamValue(String paramValue) {
        this.paramValue = paramValue;
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

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public ComponentConfig getComponentConfig() {
        return componentConfig;
    }

    public void setComponentConfig(ComponentConfig componentConfig) {
        this.componentConfig = componentConfig;
    }

    @Override
    public String toString() {
        return "TaskConfig{" +
                "configId='" + configId + '\'' +
                ", paramId='" + paramId + '\'' +
                ", paramValue='" + paramValue + '\'' +
                ", createTime=" + createTime +
                ", createUser='" + createUser + '\'' +
                ", taskId='" + taskId + '\'' +
                ", componentConfig=" + componentConfig +
                '}';
    }
}
