package com.chinasofti.ark.bdadp.component;

import java.util.List;

public class ParamConfig {

    public static final String PARAM = "param";
    public static final String NAME = "name";
    public static final String DESC = "description";
    public static final String TYPE = "type";
    public static final String NULLABLE = "nullable";
    public static final String DEFAULT_VAL = "defaultValue";
    public static final String DEFAULT_OPTIONS = "defaultOptions";


    private String id;
    private String name;
    private String desc;
    private ParamType type;
    private Boolean nullable;
    private String defaultVal;
    private List<ParamOption> defaultOptions;

    public ParamConfig() {

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

    public String getType() {
        return this.type.name().toLowerCase();
    }

    public void setType(ParamType type) {
        if (type != null) {
            this.type = type;
        } else {
            this.type = ParamType.TEXT;
        }
    }

    public Boolean getNullable() {
        return nullable;
    }

    public void setNullable(Boolean nullable) {
        if (nullable != null) {
            this.nullable = nullable;
        } else {
            this.nullable = true;
        }
    }

    public String getDefaultVal() {
        return defaultVal;
    }

    public void setDefaultVal(String defaultVal) {
        this.defaultVal = defaultVal;
    }

    public List<ParamOption> getDefaultOptions() {
        return defaultOptions;
    }

    public void setDefaultOptions(List<ParamOption> defaultOptions) {
        this.defaultOptions = defaultOptions;
    }

    public static enum ParamType {
        TEXT, NUMBER, TEXTAREA, OPTIONS, CHECKBOX, RADIO, FILE, DATE, PASSWORD, EXPR;

        public static ParamType newValueOf(String type) {
            if (type == null) {
                return null;
            }
            return ParamType.valueOf(type.toUpperCase());
        }
    }
}
