package com.chinasofti.ark.bdadp.component.api.channel;

import com.chinasofti.ark.bdadp.component.api.data.Data;
import java8.util.Optional;

/**
 * Created by White on 2017/1/4.
 */
public class MemoryChannel extends AbstractChannel {

    Optional<Data> optional = Optional.empty();

    @Override
    public void input(Data data) {
        optional = Optional.of(data);
    }

    @Override
    public Data output() {
        return optional.get();
    }


}
