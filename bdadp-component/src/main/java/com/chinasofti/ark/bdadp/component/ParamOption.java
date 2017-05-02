package com.chinasofti.ark.bdadp.component;

import java.util.List;

public class ParamOption {

    public static final String OPTION = "option";
    public static final String ARGS = "args";

    public static final String NAME = "name";
    public static final String DESC = "desc";
    public static final String VALUE = "value";
    public static final String CATE = "cate";

    private String name;
    private String desc;
    private String val;
    private String cate;
    private List<ParamArgument> args;

    public ParamOption() {
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

    public String getCate() {
        return cate;
    }

    public void setCate(String cate) {
        this.cate = cate;
    }

    public List<ParamArgument> getArgs() {
        return args;
    }

    public void setArgs(List<ParamArgument> args) {
        this.args = args;
    }
}
