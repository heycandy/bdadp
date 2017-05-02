package com.chinasofti.ark.bdadp.component.support;

import com.chinasofti.ark.bdadp.component.api.channel.Channel;
import com.chinasofti.ark.bdadp.component.api.data.Data;
import com.chinasofti.ark.bdadp.component.api.data.DataType;
import com.chinasofti.ark.bdadp.component.api.data.SparkData;
import com.chinasofti.ark.bdadp.component.api.data.StringData;
import com.chinasofti.ark.bdadp.component.api.options.ScenarioOptions;
import com.chinasofti.ark.bdadp.component.api.options.SparkScenarioOptions;
import com.chinasofti.ark.bdadp.component.api.sink.SinkComponent;
import com.chinasofti.ark.bdadp.component.api.sink.SparkSinkAdapter;
import com.chinasofti.ark.bdadp.component.api.sink.StringSinkAdapter;
import com.google.common.collect.Lists;
import java8.util.stream.Collectors;
import java8.util.stream.StreamSupport;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created by White on 2017/1/4.
 */
public class SinkTask extends SimpleTask<SinkComponent> implements ChannelInputable {

    private final Class<? extends Data> inputT;

    private final Collection<Channel> iChannel;

    public SinkTask(String id, String name, ScenarioOptions options,
                    Class<SinkComponent> clazz) throws IOException {
        super(id, name, options, clazz);

        Type type = this._clazz.getGenericSuperclass();
        Type[] types = ((ParameterizedType) type).getActualTypeArguments();

        inputT = (Class) types[0];
        iChannel = Lists.newArrayList();
    }

    @Override
    protected void execute() throws Exception {
        Map<DataType, List<Channel>> entrySet = StreamSupport.stream(this.getIChannel())
                .collect(Collectors.groupingBy(channel -> channel.output().getType()));

        StreamSupport.stream(entrySet.entrySet()).forEach(entry -> {
            DataType dataType = entry.getKey();
            List<Channel> channels = entry.getValue();

            Data input;
            if (dataType == DataType.SPARK && this.obj instanceof SparkSinkAdapter) {
                input = this.options().as(SparkScenarioOptions.class)
                        .union(channels)
                        .output();
                ((SparkSinkAdapter) this.obj).apply((SparkData) input);
            } else if (dataType == DataType.STRING && this.obj instanceof StringSinkAdapter) {
                input = this.options().as(ScenarioOptions.class)
                        .union(channels)
                        .output();
                ((StringSinkAdapter) this.obj).apply((StringData) input);
            } else {
                StreamSupport.stream(channels)
                        .map(Channel::output)
                        .forEach(this.obj::apply);
            }
        });

    }

    @Override
    public Collection<Channel> getIChannel() {
        return iChannel;
    }

    @Override
    public void addIChannel(Channel e) {
        this.iChannel.add(e);
        e.setOutputType(this.inputT);
    }

}
