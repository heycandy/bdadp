package com.chinasofti.ark.bdadp.entity.components;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Created by Administrator on 2016/8/29.
 */
@Entity
public class ComponentConfig implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "param_id")
    @JsonProperty("param_id")
    private String paramId;

    @Column(name = "param_name")
    @JsonProperty("param_name")
    private String paramName;

    @Column(name = "param_desc")
    @JsonProperty("param_desc")
    private String paramDesc;

    @Column(name = "param_type")
    @JsonProperty("param_type")
    private String paramType;

    @Column(name = "default_value")
    @JsonProperty("default_value")
    private String defaultValue;

    @Column(name = "default_options")
    @JsonProperty("default_options")
    private String defaultOptions;

    private boolean nullable;

    @Column(name = "order_id")
    @JsonProperty("order_id")
    private int orderId;

    @Column(name = "component_id")
    @JsonProperty("component_id")
    private String componentId;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getParamId() {
        return paramId;
    }

    public void setParamId(String paramId) {
        this.paramId = paramId;
    }

    public String getParamName() {
        return paramName;
    }

    public void setParamName(String paramName) {
        this.paramName = paramName;
    }

    public String getParamDesc() {
        return paramDesc;
    }

    public void setParamDesc(String paramDesc) {
        this.paramDesc = paramDesc;
    }

    public String getParamType() {
        return paramType;
    }

    public void setParamType(String paramType) {
        this.paramType = paramType;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String getDefaultOptions() {
        return defaultOptions;
    }

    public void setDefaultOptions(String defaultOptions) {
        this.defaultOptions = defaultOptions;
    }

    public boolean isNullable() {
        return nullable;
    }

    public void setNullable(boolean nullable) {
        this.nullable = nullable;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getComponentId() {
        return componentId;
    }

    public void setComponentId(String componentId) {
        this.componentId = componentId;
    }

    @Override
    public String toString() {
        return "ComponentConfig{" +
                "paramId='" + paramId + '\'' +
                ", paramName='" + paramName + '\'' +
                ", paramDesc='" + paramDesc + '\'' +
                ", paramType='" + paramType + '\'' +
                ", defaultValue='" + defaultValue + '\'' +
                ", defaultOptions='" + defaultOptions + '\'' +
                ", nullable=" + nullable + '\'' +
                ", orderId=" + orderId + '\'' +
                ", componentId='" + componentId + '\'' +
                '}';
    }
}
