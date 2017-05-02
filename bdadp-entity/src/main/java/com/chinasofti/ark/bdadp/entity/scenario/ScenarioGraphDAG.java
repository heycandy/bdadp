package com.chinasofti.ark.bdadp.entity.scenario;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * Created by zhangweigang on 2016/08/27.
 */
@Entity
public class ScenarioGraphDAG implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    //作业图 ID
    @Id
    @Column(name = "graph_id")
    @JsonProperty("graph_id")
    private String graphId;
    //作业图原始数据
    @Column(name = "graph_raw")
    @JsonProperty("graph_raw")
    private String graphRaw;
    //关联图Vertex
    @OneToMany(fetch = FetchType.EAGER)
    @Fetch(FetchMode.SUBSELECT)
    @JoinColumn(name = "graph_id")
    @JsonProperty("graph_vertexs")
    private List<ScenarioGraphVertex> graphVertexs;
    //关联图Edge
    @OneToMany(fetch = FetchType.EAGER)
    @Fetch(FetchMode.SUBSELECT)
    @JoinColumn(name = "graph_id")
    @JsonProperty("graph_edges")
    private List<ScenarioGraphEdge> graphEdges;

    //作业图顶点数
    @Column(name = "vertex_num")
    @JsonProperty("vertex_num")
    private int vertexNum;

    //作业图边数量
    @Column(name = "edge_num")
    @JsonProperty("edge_num")
    private int edgeNum;

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

    //场景ID
    @Column(name = "scenario_id")
    @JsonProperty("scenario_id")
    private String scenarioId;
    //场景版本ID
    @Column(name = "version_id")
    @JsonProperty("version_id")
    private String versionId;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getGraphId() {
        return graphId;
    }

    public void setGraphId(String graphId) {
        this.graphId = graphId;
    }

    public String getGraphRaw() {
        return graphRaw;
    }

    public void setGraphRaw(String graphRaw) {
        this.graphRaw = graphRaw;
    }

    public List<ScenarioGraphVertex> getGraphVertexs() {
        return graphVertexs;
    }

    public void setGraphVertexs(List<ScenarioGraphVertex> graphVertexs) {
        this.graphVertexs = graphVertexs;
    }

    public List<ScenarioGraphEdge> getGraphEdges() {
        return graphEdges;
    }

    public void setGraphEdges(List<ScenarioGraphEdge> graphEdges) {
        this.graphEdges = graphEdges;
    }

    public int getVertexNum() {
        return vertexNum;
    }

    public void setVertexNum(int vertexNum) {
        this.vertexNum = vertexNum;
    }

    public int getEdgeNum() {
        return edgeNum;
    }

    public void setEdgeNum(int edgeNum) {
        this.edgeNum = edgeNum;
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

    public String getScenarioId() {
        return scenarioId;
    }

    public void setScenarioId(String scenarioId) {
        this.scenarioId = scenarioId;
    }

    public String getVersionId() {
        return versionId;
    }

    public void setVersionId(String versionId) {
        this.versionId = versionId;
    }

    @Override
    public String toString() {
        return "ScenarioGraphDAG{" +
                "graphId='" + graphId + '\'' +
                ", graphRaw='" + graphRaw + '\'' +
                ", graphVertexs=" + graphVertexs +
                ", graphEdges=" + graphEdges +
                ", vertexNum=" + vertexNum +
                ", edgeNum=" + edgeNum +
                ", createTime=" + createTime +
                ", createUser='" + createUser + '\'' +
                ", modifiedTime=" + modifiedTime +
                ", modifiedUser='" + modifiedUser + '\'' +
                ", scenarioId='" + scenarioId + '\'' +
                ", versionId='" + versionId + '\'' +
                '}';
    }
}
