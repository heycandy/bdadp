package com.chinasofti.ark.bdadp.entity.scenario;

import com.chinasofti.ark.bdadp.entity.task.Task;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by zhangweigang on 2016/08/27.
 */
@Entity
public class ScenarioGraphVertex implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    //顶点 ID
    @Id
    @Column(name = "vertex_id")
    @JsonProperty("vertex_id")
    private String vertexId;
    //顶点KEY
    @Column(name = "key_id")
    @JsonProperty("key")
    private int keyId;
    //是否根顶点
    @Column(name = "is_top")
    @JsonProperty("is_top")
    private boolean isTop;
    //是否结束顶点
    @Column(name = "is_terminal")
    @JsonProperty("is_terminal")
    private boolean isTerminal;

    @Column(name = "create_time")
    @JsonProperty("create_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @Column(name = "create_user")
    @JsonProperty("create_user")
    private String createUser;

    //关联作业
    @OneToOne
    @JoinColumn(name = "task_id")
    @JsonProperty("task")
    private Task task;
    //作业图 ID
    @Column(name = "graph_id")
    @JsonProperty("graph_id")
    private String graphId;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getVertexId() {
        return vertexId;
    }

    public void setVertexId(String vertexId) {
        this.vertexId = vertexId;
    }

    public int getKeyId() {
        return keyId;
    }

    public void setKeyId(int keyId) {
        this.keyId = keyId;
    }

    public boolean isTop() {
        return isTop;
    }

    public void setTop(boolean isTop) {
        this.isTop = isTop;
    }

    public boolean isTerminal() {
        return isTerminal;
    }

    public void setTerminal(boolean isTerminal) {
        this.isTerminal = isTerminal;
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

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public String getGraphId() {
        return graphId;
    }

    public void setGraphId(String graphId) {
        this.graphId = graphId;
    }

    @Override
    public String toString() {
        return "ScenarioGraphVertex{" +
                "vertexId='" + vertexId + '\'' +
                ", keyId=" + keyId +
                ", isTop=" + isTop +
                ", isTerminal=" + isTerminal +
                ", createTime=" + createTime +
                ", createUser='" + createUser + '\'' +
                ", task=" + task +
                ", graphId='" + graphId + '\'' +
                '}';
    }
}
