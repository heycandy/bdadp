package com.chinasofti.ark.bdadp.service.scenario.impl;

import com.chinasofti.ark.bdadp.component.api.channel.Channel;
import com.chinasofti.ark.bdadp.component.api.channel.MemoryChannel;
import com.chinasofti.ark.bdadp.component.api.options.PipelineOptionsFactory;
import com.chinasofti.ark.bdadp.component.api.options.ScenarioOptions;
import com.chinasofti.ark.bdadp.component.api.options.SparkScenarioOptions;
import com.chinasofti.ark.bdadp.component.api.sink.SinkComponent;
import com.chinasofti.ark.bdadp.component.api.source.SourceComponent;
import com.chinasofti.ark.bdadp.component.api.transforms.MultiTransComponent;
import com.chinasofti.ark.bdadp.component.api.transforms.TransformableComponent;
import com.chinasofti.ark.bdadp.component.support.*;
import com.chinasofti.ark.bdadp.entity.scenario.ScenarioGraphEdge;
import com.chinasofti.ark.bdadp.service.components.ComponentService;
import com.chinasofti.ark.bdadp.service.graph.bean.Edge;
import com.chinasofti.ark.bdadp.service.graph.bean.SimpleEdge;
import com.chinasofti.ark.bdadp.service.graph.bean.TaskVertex;
import com.chinasofti.ark.bdadp.service.graph.bean.Vertex;
import com.google.common.base.Charsets;
import com.google.common.base.Joiner;
import com.google.common.collect.Maps;
import com.google.common.io.Files;
import java8.util.stream.Collectors;
import java8.util.stream.StreamSupport;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Map;
import java.util.Properties;

/**
 * Created by White on 2016/09/18.
 */

public class ScenarioExecutorServiceImpl$Channel
        extends ScenarioExecutorServiceImpl implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Override
    Collection<Edge> buildEdges(Collection<ScenarioGraphEdge> edges, Map<Integer, Vertex> vertexMap) {
        return StreamSupport.stream(edges)
                .map(e -> {
                    String edgeId = e.getEdgeId();
                    Vertex fromVertex = vertexMap.get(e.getFromKey());
                    Vertex toVertex = vertexMap.get(e.getToKey());

                    if (fromVertex instanceof TaskVertex && toVertex instanceof TaskVertex) {
                        com.chinasofti.ark.bdadp.component.support.Task
                                fromTask =
                                ((TaskVertex) fromVertex).get();
                        com.chinasofti.ark.bdadp.component.support.Task
                                toTask =
                                ((TaskVertex) toVertex).get();

                        Channel channel = new MemoryChannel();

                        if (fromTask instanceof ChannelOutputable) {
                            ((ChannelOutputable) fromTask).addOChannel(channel);
                        }

                        if (toTask instanceof ChannelInputable) {
                            ((ChannelInputable) toTask).addIChannel(channel);
                        }
                    }

                    return new SimpleEdge(edgeId, fromVertex, toVertex);
                }).collect(Collectors.toList());
    }

    @Override
    com.chinasofti.ark.bdadp.component.support.Task
    newSimpleTask(
            String taskId, String taskName, ScenarioOptions options, String relationId)
            throws ClassNotFoundException, IOException {

        Class clazz = componentService.loadComponentClass(relationId);

        if (TransformableComponent.class.isAssignableFrom(clazz)) {
            return new TransformableTask(taskId, taskName, options, clazz);
        } else if (MultiTransComponent.class.isAssignableFrom(clazz)) {
            return new MultiTransformableTask(taskId, taskName, options, clazz);
        } else if (SourceComponent.class.isAssignableFrom(clazz)) {
            return new SourceTask(taskId, taskName, options, clazz);
        } else if (SinkComponent.class.isAssignableFrom(clazz)) {
            return new SinkTask(taskId, taskName, options, clazz);
        } else {
            return new CacheableTask(taskId, taskName, options, clazz);
        }

    }

    @Override
    protected Map<String, String> defaultSettings() throws Exception {
        Map<String, String> settings = Maps.newConcurrentMap();

        // 3、sparkconf
        URL resource = this.getClass().getClassLoader().getResource("spark-defaults.conf");
        if (resource != null) {
            File file = new File(resource.getPath());
            Reader reader = Files.newReader(file, Charsets.UTF_8);

            Properties properties = new Properties();
            properties.load(reader);

            reader.close();

            settings.putAll(Maps.fromProperties(properties));
        } else {
            File file = Paths.get(System.getProperty("user.hadoop.conf"), "spark-defaults.conf").toFile();
            if (file.exists()) {
                Reader reader = Files.newReader(file, Charsets.UTF_8);

                Properties properties = new Properties();
                properties.load(reader);

                reader.close();

                settings.putAll(Maps.fromProperties(properties));
            }
        }

        // 2、properties
        Properties localProperties = (Properties) this.applicationContext
                .getBean(PropertySourcesPlaceholderConfigurer.class)
                .getAppliedPropertySources().get("localProperties").getSource();

        for (String key : localProperties.stringPropertyNames()) {
            if (key.startsWith("executor.service.spark")) {
                settings.put(key.substring("executor.service.".length()), localProperties.getProperty(key));
            }
        }

        // component.jars
        Iterable<String> parts = this.applicationContext
                .getBean(ComponentService.class).getJarFiles();

        String val = Joiner.on(",").join(parts);

        // spark.jars
        if (settings.containsKey("spark.jars")) {
            val = val + "," + settings.get("spark.jars");
        }
        settings.put("spark.jars", val);

        return settings;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;

        try {
            Map<String, String> defaultSettings = defaultSettings();
            if (defaultSettings.containsKey("spark.app.name") &&
                    defaultSettings.containsKey("spark.master")) {
                PipelineOptionsFactory.fromSettings(defaultSettings)
                        .as(ScenarioOptions.class).setDebug(true)
                        .as(SparkScenarioOptions.class);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);

        }

    }
}
