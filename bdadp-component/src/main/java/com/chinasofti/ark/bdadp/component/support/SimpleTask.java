package com.chinasofti.ark.bdadp.component.support;

import com.chinasofti.ark.bdadp.component.api.CallableComponent;
import com.chinasofti.ark.bdadp.component.api.Component;
import com.chinasofti.ark.bdadp.component.api.Configureable;
import com.chinasofti.ark.bdadp.component.api.RunnableComponent;
import com.chinasofti.ark.bdadp.component.api.data.Data;
import com.chinasofti.ark.bdadp.component.api.options.ScenarioOptions;
import org.slf4j.Logger;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by White on 2016/09/04.
 */
public class SimpleTask<K extends Component> extends AbstractTask<Data> {

    protected final Class<K> _clazz;

    protected K obj = null;

    public SimpleTask(String id, String name, ScenarioOptions options, Class<K> clazz)
            throws IOException {
        super(id, name, TaskLogProvider.getLog(id, options.getExecutionId()), options);

        _clazz = clazz;
    }

    protected void init()
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException,
            InstantiationException {
        Constructor<K>
                constructor =
                this._clazz.getConstructor(String.class, String.class, Logger.class);
        this.obj = constructor.newInstance(getId(), getName(), getLog());

    }

    protected void configure() {
        if (this.obj instanceof Configureable) {
            ((Configureable) this.obj).configure(props);
        }
    }

    protected void execute() throws Exception {
        if (this.obj instanceof RunnableComponent) {
            ((RunnableComponent) obj).run();
        } else if (this.obj instanceof CallableComponent) {
            ((CallableComponent) this.obj).call();
        } else {
            throw new RuntimeException("TypeCheckError");
        }
    }

    protected void exception(Exception e) {
        throw new RuntimeException(e);
    }

    protected void finish() {
        TaskLogProvider.close(getLog());
    }


    @Override
    public void run() {
        setState(TaskState.COMPLETING.name());
        try {
            info(String.format("init %s.", this));
            this.init();
            info(String.format("configure %s.", this));
            this.configure();
            info(String.format("execute %s.", this));
            this.execute();

        } catch (Exception e) {
            setState(TaskState.EXCEPTIONAL.name());
            error(String.format("exception %s.", this), e);
            this.finish();
            this.exception(e);
        }

        setState(TaskState.NORMAL.name());
        info(String.format("finish %s.", this));
        this.finish();
    }

    @Override
    public double getProgress() throws Exception {
        return 0;
    }

    public String getScenarioId() {
        return this.options().as(ScenarioOptions.class).getScenarioId();
    }

    public String getExecutionId() {
        return this.options().as(ScenarioOptions.class).getExecutionId();
    }

}
