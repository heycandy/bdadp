package com.chinasofti.ark.bdadp.component.support;

import com.chinasofti.ark.bdadp.component.ComponentProps;
import com.chinasofti.ark.bdadp.component.api.CallableComponent;
import com.chinasofti.ark.bdadp.component.api.Component;
import com.chinasofti.ark.bdadp.component.api.Configureable;
import com.chinasofti.ark.bdadp.component.api.RunnableComponent;
import com.chinasofti.ark.bdadp.component.api.options.ScenarioOptions;
import com.google.common.base.Strings;

import java.io.IOException;
import java.lang.reflect.Constructor;

/**
 * Created by White on 2016/09/04.
 */
public class CacheableTask extends SimpleTask<Component> {

    public CacheableTask(String id, String name, ScenarioOptions options, Class<Component> clazz)
            throws IOException {
        super(id, name, options, clazz);
    }

    @Override
    public void run() {
        setState(TaskState.COMPLETING.name());
        try {
            info(String.format("init %s.", this));
            Constructor constructor = this._clazz.getConstructors()[0];
            Object obj = constructor.newInstance(getId(), getName(), getLog());

            // multi source
            if (!Strings.isNullOrEmpty(this.props.getString("file_source")) &&
                    !Strings.isNullOrEmpty(this.props.getString("file_path"))) {
                String[] fileSources = this.props.getString("file_source").split(",");
                String[] filePaths = this.props.getString("file_path").split(",");

                for (int i = 0; i < fileSources.length; i++) {
                    String fileSource = fileSources[i];
                    String filePath = filePaths[i];

                    ComponentProps props = new ComponentProps();

                    props.copy(this.props);
                    props.setProperty("file_source", fileSource);
                    props.setProperty("file_path", filePath);

                    this.runOnce(obj, props);
                }

            } else {
                this.runOnce(obj, this.props);
            }

        } catch (Exception e) {
            setState(TaskState.EXCEPTIONAL.name());
            error(String.format("exception %s.", this), e);

            TaskLogProvider.close(getLog());
            throw new RuntimeException(e);
        }

        setState(TaskState.NORMAL.name());
        info(String.format("finish %s.", this));

        TaskLogProvider.close(getLog());
    }

    protected void runOnce(Object obj, ComponentProps props) throws Exception {
        if (obj instanceof Configureable) {
            info(String.format("configure %s.", this));
            ((Configureable) obj).configure(props);
        }

        info(String.format("execute %s.", this));
        if (obj instanceof RunnableComponent) {
            ((RunnableComponent) obj).run();
        } else if (obj instanceof CallableComponent) {
            ((CallableComponent) obj).call();
        } else {
            throw new RuntimeException("TypeCheckError");
        }
    }

    @Override
    public void configure(ComponentProps props) {
        if (this.props.isEmpty()) {
            super.configure(props);
        } else {
            if (this.props.getKeySet().contains("file_path") && Strings
                    .isNullOrEmpty(this.props.getString("file_path"))) {
                this.props.setProperty("file_source", props.getString("file_source"));
                this.props.setProperty("file_path", props.getString("file_path"));
            }

            if (this.props.getKeySet().contains("dest_path") && Strings
                    .isNullOrEmpty(this.props.getString("dest_path"))) {
                this.props.setProperty("dest_source", props.getString("dest_source"));
                this.props.setProperty("dest_path", props.getString("dest_path"));
            }

            if (this.props.getKeySet().contains("hive_conf") && Strings
                    .isNullOrEmpty(this.props.getString("hive_conf"))) {
                this.props.setProperty("hive_conf", props.getString("hive_conf"));
            }
        }
    }

}
