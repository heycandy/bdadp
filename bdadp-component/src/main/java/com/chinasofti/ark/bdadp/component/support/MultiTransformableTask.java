package com.chinasofti.ark.bdadp.component.support;

import com.chinasofti.ark.bdadp.component.api.channel.Channel;
import com.chinasofti.ark.bdadp.component.api.data.Data;
import com.chinasofti.ark.bdadp.component.api.options.ScenarioOptions;
import com.chinasofti.ark.bdadp.component.api.transforms.MultiTransComponent;
import com.google.common.collect.Lists;
import java8.util.stream.Collectors;
import java8.util.stream.StreamSupport;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;


/**
 * Created by White on 2017/1/4.
 */
public class MultiTransformableTask extends SimpleTask<MultiTransComponent>
        implements ChannelInputable, ChannelOutputable {

    private final Class<? extends Data> inputT;
    private final Class<? extends Data> outputT;

    private final Collection<Channel> iChannel;
    private final Collection<Channel> oChannel;

    public MultiTransformableTask(String id, String name, ScenarioOptions options,
                                  Class<MultiTransComponent> clazz)
            throws IOException {
        super(id, name, options, clazz);

        Type type = this._clazz.getGenericSuperclass();
        Type[] types = ((ParameterizedType) type).getActualTypeArguments();

        inputT = (Class) ((ParameterizedType) types[0]).getActualTypeArguments()[0];
        outputT = (Class) types[1];

        iChannel = Lists.newArrayList();
        oChannel = Lists.newArrayList();
    }

    @Override
    protected void execute() throws Exception {
        Collection<Data> input = StreamSupport.stream(this.getIChannel())
                .map(Channel::output)
                .collect(Collectors.toList());

        Data output = this.obj.apply(input);

        StreamSupport.stream(this.getOChannel())
                .forEach(channel -> channel.input(output));
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
