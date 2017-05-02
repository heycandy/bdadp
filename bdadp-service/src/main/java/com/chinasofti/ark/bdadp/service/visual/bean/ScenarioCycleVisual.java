package com.chinasofti.ark.bdadp.service.visual.bean;

/**
 * Created by White on 2016/10/22.
 */
public class ScenarioCycleVisual {

    private Integer usage;
    private Integer success;
    private Integer failure;

    public ScenarioCycleVisual() {
    }

    public ScenarioCycleVisual(Integer usage, Integer success, Integer failure) {
        this.usage = usage;
        this.success = success;
        this.failure = failure;
    }

    public Integer getUsage() {
        return usage;
    }

    public void setUsage(Integer usage) {
        this.usage = usage;
    }

    public Integer getSuccess() {
        return success;
    }

    public void setSuccess(Integer success) {
        this.success = success;
    }

    public Integer getFailure() {
        return failure;
    }

    public void setFailure(Integer failure) {
        this.failure = failure;
    }
}
