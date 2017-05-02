package com.chinasofti.ark.bdadp.entity.components;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.util.List;

/**
 * Created by Administrator on 2016/8/27.
 */
@Entity
public class Component implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "component_id")
    @JsonProperty("component_id")
    private String componentId;

    @Column(name = "component_name")
    @JsonProperty("component_name")
    private String componentName;

    @Column(name = "component_desc")
    @JsonProperty("component_desc")
    private String componentDesc;

    @Column(name = "component_status")
    @JsonProperty("component_status")
    private Integer componentStatus;

    @Column(name = "component_pid")
    @JsonProperty("component_pid")
    private String componentPid;

    @Column(name = "component_base")
    @JsonProperty("component_base")
    private boolean componentBase;

    @Column(name = "component_type")
    @JsonProperty("component_type")
    private String componentType;

    @Transient
    private List<ComponentConfig> componentConfigs;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getComponentId() {
        return componentId;
    }

    public void setComponentId(String componentId) {
        this.componentId = componentId;
    }

    public String getComponentName() {
        return componentName;
    }

    public void setComponentName(String componentName) {
        this.componentName = componentName;
    }

    public String getComponentDesc() {
        return componentDesc;
    }

    public void setComponentDesc(String componentDesc) {
        this.componentDesc = componentDesc;
    }

    public Integer getComponentStatus() {
        return componentStatus;
    }

    public void setComponentStatus(Integer componentStatus) {
        this.componentStatus = componentStatus;
    }

    public String getComponentPid() {
        return componentPid;
    }

    public void setComponentPid(String componentPid) {
        this.componentPid = componentPid;
    }

    public boolean isComponentBase() {
        return componentBase;
    }

    public void setComponentBase(boolean componentBase) {
        this.componentBase = componentBase;
    }

    public String getComponentType() {
        return componentType;
    }

    public void setComponentType(String componentType) {
        this.componentType = componentType;
    }

    public List<ComponentConfig> getComponentConfigs() {
        return componentConfigs;
    }

    public void setComponentConfigs(List<ComponentConfig> componentConfigs) {
        this.componentConfigs = componentConfigs;
    }

    @Override
    public String toString() {
        return "Component{" +
                "componentId='" + componentId + '\'' +
                ", componentName='" + componentName + '\'' +
                ", componentDesc='" + componentDesc + '\'' +
                ", componentStatus=" + componentStatus +
                ", componentPid='" + componentPid + '\'' +
                ", componentBase=" + componentBase +
                ", componentType='" + componentType + '\'' +
                ", componentConfigs=" + componentConfigs +
                '}';
    }
}
