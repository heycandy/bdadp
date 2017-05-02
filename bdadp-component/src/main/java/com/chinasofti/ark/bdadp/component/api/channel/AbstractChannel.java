package com.chinasofti.ark.bdadp.component.api.channel;

import com.chinasofti.ark.bdadp.component.api.data.Data;

/**
 * Created by White on 2017/1/27.
 */
public abstract class AbstractChannel implements Channel {

    Class<? extends Data> inputT;
    Class<? extends Data> outputT;

    @Override
    public Class getInputType() {
        return this.inputT;
    }

    @Override
    public void setInputType(Class inputT) {
        this.inputT = inputT;
    }

    @Override
    public Class getOutputType() {
        return this.outputT;
    }

    @Override
    public void setOutputType(Class outputT) {
        this.outputT = outputT;
    }
}
