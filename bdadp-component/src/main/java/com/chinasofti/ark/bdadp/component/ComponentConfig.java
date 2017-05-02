package com.chinasofti.ark.bdadp.component;

import java.util.List;

public class ComponentConfig {

    public static final String COMPONENT_NAME = "componentName";
    public static final String COMPONENT_DESC = "componentDescription";
    public static final String COMPONENT_CLASS = "componentClass";
    public static final String COMPONENT_ICON = "componentIcon";
    public static final String COMPONENT_PID = "componentPid";
    public static final String COMPONENT_PARAMS = "params";

    private String id;
    private String name;
    private String desc;
    private String clazz;
    private String pid;

    private ComponentIcon icon;

    private List<ParamConfig> params;

    public ComponentConfig() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getClazz() {
        return clazz;
    }

    public void setClazz(String clazz) {
        this.clazz = clazz;
    }

    public ComponentIcon getIcon() {
        return icon;
    }

    public void setIcon(ComponentIcon icon) {
        this.icon = icon;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public List<ParamConfig> getParams() {
        return params;
    }

    public void setParams(List<ParamConfig> params) {
        this.params = params;
    }
}
