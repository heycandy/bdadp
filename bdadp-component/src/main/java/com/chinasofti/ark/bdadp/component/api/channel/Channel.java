package com.chinasofti.ark.bdadp.component.api.channel;

import com.chinasofti.ark.bdadp.component.api.DataInputable;
import com.chinasofti.ark.bdadp.component.api.DataOutputable;
import com.chinasofti.ark.bdadp.component.api.data.Data;

/**
 * Created by White on 2017/1/4.
 */
public interface Channel extends DataInputable, DataOutputable {

    @Override
    void input(Data data);

    @Override
    Data output();

    Class<? extends Data> getInputType();

    void setInputType(Class<? extends Data> inputT);

    Class<? extends Data> getOutputType();

    void setOutputType(Class<? extends Data> outputT);

}
