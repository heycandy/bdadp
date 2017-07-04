package com.chinasofti.ark.bdadp.component.support;

import com.chinasofti.ark.bdadp.component.ComponentProps;
import com.chinasofti.ark.bdadp.component.api.CallableComponent;
import com.chinasofti.ark.bdadp.component.api.Listener;
import com.chinasofti.ark.bdadp.component.api.RunnableComponent;
import com.chinasofti.ark.bdadp.component.api.data.Data;
import com.chinasofti.ark.bdadp.component.api.options.PipelineOptions;

import org.slf4j.Logger;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.LockSupport;

/**
 * Created by White on 2016/09/05.
 */
public abstract class AbstractTask<V extends Data> implements Task<V> {

  private final String _id;
  private final String _name;
  private final PipelineOptions _options;
  private final Set<Listener> listeners;
  protected volatile ComponentProps props;
  protected TaskState state;
  protected V result;
  private Logger _log;
  private volatile Thread runner;

  public AbstractTask(String id, String name, PipelineOptions options)
      throws IOException {
    _id = id;
    _name = name;
    _options = options;

    listeners = new HashSet<>();
    props = new ComponentProps();

    setState(TaskState.NEW.name());
  }

  public void awaitTermination(long timeout, TimeUnit unit) {

    long deadline = unit != null ? System.nanoTime() + unit.toNanos(timeout) : 0L;

    if (deadline == 0L) {
      LockSupport.park(this);
    } else {
      long nanos = deadline - System.nanoTime();
      LockSupport.parkNanos(this, nanos);
    }
  }

  // Non-public classes supporting the public methods

  @Override
  public String getId() {
    return this._id;
  }

  @Override
  public String getName() {
    return this._name;
  }

  @Override
  public abstract double getProgress() throws Exception;

  @Override
  public void configure(ComponentProps props) {
    this.props.copy(props);
  }

  @Override
  public ComponentProps props() {
    return this.props;
  }

  @Override
  public boolean cancel(boolean mayInterruptIfRunning) {
    if (!this.isCancelled() && !this.isDone()) {
      setState(TaskState.INTERRUPTING.name());
      if (mayInterruptIfRunning && this.runner != null) {
        try {
          this.runner.interrupt();
        } finally {
          setState(TaskState.INTERRUPTED.name());
        }
      }

      return true;
    } else {
      return false;
    }
  }

  @Override
  public boolean isCancelled() {
    return getState() >= TaskState.CANCELLED.ordinal();
  }

  @Override
  public boolean isDone() {
    return this.state == TaskState.NORMAL;
  }

  @Override
  public V get() throws InterruptedException, ExecutionException {
    awaitTermination(0L, null);
    return this.result;
  }

  @Override
  public V get(long timeout, TimeUnit unit)
      throws InterruptedException, ExecutionException, TimeoutException {
    awaitTermination(timeout, unit);

    return this.result;
  }

  @Override
  public void report(Listener listener) {
    try {
      listener.listen(this);
    } catch (Exception e) {
      warn(e.getMessage());
    }
  }

  @Override
  public void reportAll() {
    for (Listener listener : this.listeners) {
      report(listener);
    }
  }

  @Override
  public void addListener(Listener e) {
    this.listeners.add(e);

    report(e);
  }

  @Override
  public void removeListener(Listener o) {
    this.listeners.remove(o);
  }

  @Override
  public void removeAllListener() {
    this.listeners.removeAll(this.listeners);
  }

  @Override
  public abstract void run();

  @Override
  public int getState() {
    return this.state.ordinal();
  }

  public void setState(String name) {
    this.state = TaskState.valueOf(name);
    if (this.state == TaskState.COMPLETING) {
      try {
        this._log = TaskLogProvider.getLog(_id, _options.getExecutionId());
      } catch (Exception e) {
        e.printStackTrace();
      }
    }

    reportAll();
  }

  protected PipelineOptions options() {
    return _options;
  }

  protected Logger getLog() {
    return this._log;
  }

  protected void debug(String message) {
    this._log.debug(message);
  }

  protected void debug(String message, Throwable t) {
    this._log.debug(message, t);
  }

  protected void info(String message) {
    this._log.info(message);
  }

  protected void info(String message, Throwable t) {
    this._log.info(message, t);
  }

  protected void warn(String message) {
    this._log.warn(message);
  }

  protected void warn(String message, Throwable t) {
    this._log.warn(message, t);
  }

  protected void error(String message) {
    this._log.error(message);
  }

  protected void error(String message, Throwable t) {
    this._log.error(message, t);
  }

  @Override
  public String toString() {
    return "Task{" +
           "_id='" + _id + '\'' +
           ", _name='" + _name + '\'' +
           ", state=" + state +
           '}';
  }

  /**
   * A callable that runs given task and returns given result
   */
  static final class RunnableAdapter<V> extends CallableComponent<V> {

    final RunnableComponent task;
    final V result;

    public RunnableAdapter(RunnableComponent task, V result) {
      super(task.getId(), task.getName(), task.getLog());
      this.task = task;
      this.result = result;
    }

    @Override
    public V call() throws Exception {
      task.run();
      return result;
    }
  }
}
