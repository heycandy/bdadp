package com.chinasofti.ark.bdadp.entity.task;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * Created by TongTong on 2016/08/29.
 */
@Entity
public class Task implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    //作业ID
    @Id
    @Column(name = "task_id")
    @JsonProperty("task_id")
    private String taskId;
    //作业名称
    @Column(name = "task_name")
    @JsonProperty("task_name")
    private String taskName;
    //作业描述
    @Column(name = "task_desc")
    @JsonProperty("task_desc")
    private String taskDesc;
    //作业类型
    @Column(name = "task_type")
    @JsonProperty("task_type")
    private String taskType;
    //场景状态
    @Column(name = "task_status")
    @JsonProperty("task_status")
    private int taskStatus;
    //创建时间
    @Column(name = "create_time")
    @JsonProperty("create_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
    //创建操作人
    @Column(name = "create_user")
    @JsonProperty("create_user")
    private String createUser;
    //关联ID
    @Column(name = "relation_id")
    @JsonProperty("relation_id")
    private String relationId;
    //关联场景版本ID
    @Column(name = "version_id")
    @JsonProperty("version_id")
    private String versionId;

    //关联作业配置
    @OneToMany(fetch = FetchType.EAGER)
    @Fetch(FetchMode.SUBSELECT)
    @JoinColumn(name = "task_id")
    @OrderBy(value = "createTime")
    @JsonProperty("task_configs")
    private List<TaskConfig> taskConfigs;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskDesc() {
        return taskDesc;
    }

    public void setTaskDesc(String taskDesc) {
        this.taskDesc = taskDesc;
    }

    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    public int getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(int taskStatus) {
        this.taskStatus = taskStatus;
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

    public String getRelationId() {
        return relationId;
    }

    public void setRelationId(String relationId) {
        this.relationId = relationId;
    }

    public String getVersionId() {
        return versionId;
    }

    public void setVersionId(String versionId) {
        this.versionId = versionId;
    }

    public List<TaskConfig> getTaskConfigs() {
        return taskConfigs;
    }

    public void setTaskConfigs(List<TaskConfig> taskConfigs) {
        this.taskConfigs = taskConfigs;
    }

    @Override
    public String toString() {
        return "Task{" +
                "taskId='" + taskId + '\'' +
                ", taskName='" + taskName + '\'' +
                ", taskDesc='" + taskDesc + '\'' +
                ", taskType='" + taskType + '\'' +
                ", taskStatus=" + taskStatus +
                ", createTime=" + createTime +
                ", createUser='" + createUser + '\'' +
                ", relationId='" + relationId + '\'' +
                ", versionId='" + versionId + '\'' +
                ", taskConfigs=" + taskConfigs +
                '}';
    }
}
