package com.chinasofti.ark.bdadp.component;

public class ParamArgument {

    public static final String ARG = "arg";

    public static final String NAME = "name";
    public static final String DESC = "desc";
    public static final String VALUE = "value";
    public static final String DATA_TYPE = "dataType";

    private String name;
    private String desc;
    private String val;
    private String dataType;

    public ParamArgument() {
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

    public String getVal() {
        return val;
    }

    public void setVal(String val) {
        this.val = val;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }
}
