package com.chinasofti.ark.bdadp.component.support;

import com.google.common.collect.Lists;

import com.chinasofti.ark.bdadp.component.api.channel.Channel;
import com.chinasofti.ark.bdadp.component.api.data.Data;
import com.chinasofti.ark.bdadp.component.api.options.ScenarioOptions;
import com.chinasofti.ark.bdadp.component.api.options.SparkScenarioOptions;
import com.chinasofti.ark.bdadp.component.api.source.SourceComponent;
import com.chinasofti.ark.bdadp.component.api.source.SparkSourceAdapter;
import com.chinasofti.ark.bdadp.component.api.source.StreamSourceAdapter;
import com.chinasofti.ark.bdadp.component.api.source.StringSourceAdapter;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import java8.util.stream.Collectors;
import java8.util.stream.StreamSupport;

/**
 * Created by White on 2017/1/4.
 */
public class SourceTask extends SimpleTask<SourceComponent> implements ChannelOutputable {

    private final Class<? extends Data> outputT;

    private final Collection<Channel> oChannel;

    public SourceTask(String id, String name, ScenarioOptions options, Class<SourceComponent> clazz)
            throws IOException {
        super(id, name, options, clazz);

        Type type = this._clazz.getGenericSuperclass();
        Type[] types = ((ParameterizedType) type).getActualTypeArguments();

        outputT = (Class) types[0];
        oChannel = Lists.newArrayList();
    }

    @Override
    protected void execute() throws Exception {
        Map<Class<? extends Data>, List<Channel>> entrySet = StreamSupport.stream(this.getOChannel())
                .collect(Collectors.groupingBy(Channel::getOutputType));

        StreamSupport.stream(entrySet.entrySet()).forEach(entry -> {
            Class<? extends Data> outputT = entry.getKey();
            List<Channel> channels = entry.getValue();
            Data data;
          if (this.obj instanceof SparkSourceAdapter) {
                data = ((SparkSourceAdapter) this.obj).spark(options().as(SparkScenarioOptions.class));
          } else if (this.obj instanceof StreamSourceAdapter) {
            data =
                ((StreamSourceAdapter) this.obj).stream(options().as(SparkScenarioOptions.class));
          } else if (this.obj instanceof StringSourceAdapter) {
                data = ((StringSourceAdapter) this.obj).string();
            } else {
                data = this.obj.call();
            }

            StreamSupport.stream(channels)
                    .forEach(channel -> channel.input(data));
        });
    }

    @Override
    public Collection<Channel> getOChannel() {
        return oChannel;
    }

    @Override
    public void addOChannel(Channel e) {
        this.oChannel.add(e);
        e.setInputType(this.outputT);
    }
}
