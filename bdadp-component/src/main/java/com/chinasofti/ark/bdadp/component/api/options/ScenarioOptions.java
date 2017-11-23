package com.chinasofti.ark.bdadp.component.api.options;

import com.google.common.collect.Maps;

import com.chinasofti.ark.bdadp.component.api.channel.Channel;
import com.chinasofti.ark.bdadp.component.api.channel.MemoryChannel;
import com.chinasofti.ark.bdadp.component.api.data.Data;
import com.chinasofti.ark.bdadp.component.api.data.DataType;
import com.chinasofti.ark.bdadp.component.api.data.StringData;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Map;

import java8.util.stream.StreamSupport;

public class ScenarioOptions implements PipelineOptions {

    protected Map<Class, PipelineOptions> klass = Maps.newHashMap();
    protected Map<String, String> settings;

    private String scenarioId;
    private String scenarioName;
    private String tempLocation;
    private String executionId;
    private boolean debug;
  private boolean isStreaming;

    public ScenarioOptions() {
        this(Maps.newConcurrentMap());
    }

    public ScenarioOptions(Map<String, String> settings) {
        this.settings = settings;
    }

    public ScenarioOptions(ScenarioOptions options) {
        this.setDebug(options.isDebug());
        this.setExecutionId(options.getExecutionId());
        this.setSettings(options.getSettings());
        this.setScenarioId(options.getScenarioId());
        this.setScenarioName(options.getScenarioName());
        this.setTempLocation(options.getTempLocation());

    }

    public Map<String, String> getSettings() {
        return settings;
    }

    public ScenarioOptions setSettings(Map<String, String> settings) {
        this.settings = settings;

        return this;
    }

    public String getScenarioId() {
        return scenarioId;
    }

    public ScenarioOptions setScenarioId(String scenarioId) {
        this.scenarioId = scenarioId;

        return this;
    }

    public String getScenarioName() {
        return scenarioName;
    }

    public ScenarioOptions setScenarioName(String scenarioName) {
        this.scenarioName = scenarioName;

        return this;
    }

    public String getTempLocation() {
        return tempLocation;
    }

    public ScenarioOptions setTempLocation(String tempLocation) {
        this.tempLocation = tempLocation;

        return this;
    }

    public boolean isDebug() {
        return debug;
    }

    public ScenarioOptions setDebug(boolean debug) {
        this.debug = debug;

        return this;

    }

    @Override
    public <T extends PipelineOptions> T as(Class<T> kls) {
        if (klass.containsKey(kls)) {
            return (T) klass.get(kls);
        } else {
            try {
                Constructor<T> constructor = kls.getConstructor(ScenarioOptions.class);

                PipelineOptions options = constructor.newInstance(new Object[]{this});
                klass.put(kls, options);

                return (T) options;
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    public Channel union(Collection<Channel> channels) {
        String rawData = StreamSupport.stream(channels)
                .filter(channel -> channel.output().getType() == DataType.STRING)
                .map(channel -> (String) channel.output().getRawData())
                .reduce((string, other) -> string + "\n" + other)
                .orElse("");

        Data data = new StringData(rawData);
        Channel channel = new MemoryChannel();

        channel.input(data);

        return channel;
    }

    @Override
    public String getExecutionId() {
        return this.executionId;
    }

    @Override
    public PipelineOptions setExecutionId(String id) {
        this.executionId = id;

        return this;
    }

  @Override
  public boolean isStreaming() {
    return this.isStreaming;
  }

  @Override
  public void setStreaming(boolean value) {
    this.isStreaming = value;
  }

}
