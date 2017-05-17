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
public class ScenarioGraphEdge implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    //边 ID
    @Id
    @Column(name = "edge_id")
    @JsonProperty("edge_id")
    private String edgeId;
    //边起点
    @Column(name = "from_key")
    @JsonProperty("from")
    private int fromKey;
    //边终点
    @Column(name = "to_key")
    @JsonProperty("to")
    private int toKey;

    @Column(name = "create_time")
    @JsonProperty("create_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @Column(name = "create_user")
    @JsonProperty("create_user")
    private String createUser;

    //作业图 ID
    @Column(name = "graph_id")
    @JsonProperty("graph_id")
    private String graphId;

    @Column(name = "order_id")
    @JsonProperty("order_id")
    private int orderId;

    public String getEdgeId() {
        return edgeId;
    }

    public void setEdgeId(String edgeId) {
        this.edgeId = edgeId;
    }

    public int getFromKey() {
        return fromKey;
    }

    public void setFromKey(int fromKey) {
        this.fromKey = fromKey;
    }

    public int getToKey() {
        return toKey;
    }

    public void setToKey(int toKey) {
        this.toKey = toKey;
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

    public String getGraphId() {
        return graphId;
    }

    public void setGraphId(String graphId) {
        this.graphId = graphId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    @Override
    public String toString() {
        return "ScenarioGraphEdge{" +
                "edgeId='" + edgeId + '\'' +
                ", fromKey=" + fromKey +
                ", toKey=" + toKey +
                ", createTime=" + createTime +
                ", createUser='" + createUser + '\'' +
                ", graphId='" + graphId + '\'' +
                ", orderId=" + orderId +
                '}';
    }
}
