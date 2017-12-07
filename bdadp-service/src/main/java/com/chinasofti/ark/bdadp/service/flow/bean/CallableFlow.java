package com.chinasofti.ark.bdadp.service.flow.bean;

import com.chinasofti.ark.bdadp.component.api.Listenable;
import com.chinasofti.ark.bdadp.component.api.Listener;
import com.chinasofti.ark.bdadp.component.api.Stateable;
import com.chinasofti.ark.bdadp.component.support.Future;
import com.chinasofti.ark.bdadp.component.support.TaskLogProvider;
import com.chinasofti.ark.bdadp.service.graph.bean.Graph;
import com.chinasofti.ark.bdadp.service.graph.bean.Vertex;
import com.chinasofti.ark.bdadp.service.graph.bean.VertexState;

import org.slf4j.Logger;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.LockSupport;

import java8.util.stream.StreamSupport;

/**
 * Created by White on 2016/09/21.
 */
public abstract class CallableFlow<V> extends Flow implements Future<V>, Stateable, Listenable {

  private final String _id;
  private final String _name;
  private final String _executionId;
  private final Graph _graph;
  private final Set<Listener> listeners;
  private Logger _log;
  private FlowState state;

  private volatile Thread thread;

  public CallableFlow(String id, String name, String executionId, Graph graph) {
    _id = id;
    _name = name;
    _executionId = executionId;
    _graph = graph;

    listeners = new HashSet<>();

    setState(FlowState.READY.name());
  }

  @Override
  public String getId() {
    return this._id;
  }

  @Override
  public String getName() {
    return this._name;
  }

  @Override
  public String getExecutionId() {
    return this._executionId;
  }


  @Override
  public double getProgress() throws Exception {
    return StreamSupport.stream(getGraph().getAllVertex())
               .filter(vertex -> vertex.getState() > VertexState.COMPLETING.ordinal())
               .count() / 1.0 / getGraph().getAllVertex().size();
  }
  @Override
  protected Graph getGraph() {
    return _graph;
  }

  @Override
  public int getState() {
    return this.state.ordinal();
  }

  public void setState(String name) {
    this.state = FlowState.valueOf(name);
    if (this.state.ordinal() == FlowState.COMPLETING.ordinal()) {
      thread = Thread.currentThread();

      try {
        _log = TaskLogProvider.getLog(_id, _executionId);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }

    if (this.state.ordinal() > FlowState.COMPLETING.ordinal()) {
      LockSupport.unpark(thread);
      thread = null;
    }

    reportAll();
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
    this.listeners.clear();
  }

  @Override
  public boolean cancel(boolean var1) {
    if (!this.isCancelled() && !this.isDone()) {
      if (var1 && this.thread != null) {
        try {
          this.thread.interrupt();
        } finally {
          setState(FlowState.CANCELLED.name());
        }
      }

      return true;
    } else {
      return false;
    }
  }

  @Override
  public boolean isCancelled() {
    return this.getState() == FlowState.CANCELLED.ordinal();
  }

  @Override
  public boolean isDone() {
    return this.getState() == FlowState.SUCCESS.ordinal();
  }

  @Override
  public V get() throws InterruptedException, ExecutionException {
    return null;
  }

  @Override
  public V get(long timeout, TimeUnit unit)
      throws InterruptedException, ExecutionException, TimeoutException {
    return null;
  }

  public void awaitTermination() {
    awaitTermination(0, null);
  }

  public void awaitTermination(long timeout, TimeUnit unit) {
    long nanos = unit != null ? unit.toNanos(timeout) : 0L;

    if (nanos != 0) {
      LockSupport.parkNanos(this, nanos);
    } else {
      LockSupport.park(this);
    }

  }

  public abstract CallbackExecutor onSubmit();

  public abstract CallbackExecutor onSuccess(Vertex vertex);

  public abstract CallbackExecutor onFailure(Vertex vertex, Throwable throwable);

  @Override
  public String toString() {
    return "CallableFlow{" +
           "_id='" + _id + '\'' +
           ", _name='" + _name + '\'' +
           ", state=" + state +
           '}';
  }
}
