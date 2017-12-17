package com.chinasofti.ark.bdadp.component.support;

import com.chinasofti.ark.bdadp.component.api.CallableComponent;
import com.chinasofti.ark.bdadp.component.api.Component;
import com.chinasofti.ark.bdadp.component.api.Configureable;
import com.chinasofti.ark.bdadp.component.api.Optional;
import com.chinasofti.ark.bdadp.component.api.RunnableComponent;
import com.chinasofti.ark.bdadp.component.api.data.Data;
import com.chinasofti.ark.bdadp.component.api.options.ScenarioOptions;

import org.slf4j.Logger;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by White on 2016/09/04.
 */
public class SimpleTask<K extends Component> extends AbstractTask<Data> {

  protected final ScenarioOptions _options;
  protected final Class<K> _clazz;

  protected K obj = null;

  public SimpleTask(String id, String name, ScenarioOptions options, Class<K> clazz)
      throws IOException {
    super(id, name, options);

    _options = options;
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
      // match param var value ${xxx} replace to scenario var value
      String regex = "\\$\\{(.+?)\\}";
      Pattern pattern = Pattern.compile(regex);
      for (String paramKey : props.getKeySet()) {
        String paramValue = props.getString(paramKey);
        Matcher matcher = pattern.matcher(paramValue);
        while (matcher.find()) {
          String matched = matcher.group(0);
          String key = matched.substring(2, matched.length() - 1);
          String value = _options.getSettings().get(key);
          if (value != null) {
            paramValue = paramValue.replace(matched, value);
          }
        }

        props.setProperty(paramKey, paramValue);
      }
      
      ((Configureable) this.obj).configure(props);
    }
    if (this.obj instanceof Optional) {
      ((Optional) this.obj).options(_options);
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
    setState(TaskState.NORMAL.name());
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

    info(String.format("finish %s.", this));
    this.finish();
  }

  @Override
  public boolean cancel(boolean var1) {
    if (this.obj instanceof RunnableComponent) {
      ((RunnableComponent) obj).stop();
    }

    TaskLogProvider.close(getLog());

    return super.cancel(var1);
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
