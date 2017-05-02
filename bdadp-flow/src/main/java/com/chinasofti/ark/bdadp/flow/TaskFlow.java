package com.chinasofti.ark.bdadp.flow;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.jgrapht.experimental.dag.DirectedAcyclicGraph;

public class TaskFlow {

  protected final DirectedAcyclicGraph<Task, TaskLink> dag;
  protected Task current = null;
  private Map<String, Task> forkTasks;
  private Map<String, Task> joinTasks;

  TaskFlow(DirectedAcyclicGraph<Task, TaskLink> dag) {
    this.dag = dag;
  }

  public boolean contains(Task task) {
    return this.dag.containsVertex(task);
  }

  public Task currentTask() {
    return this.current;
  }

  public Task moveTo(Task task) {
    this.current = task;
    return this.current;
  }

  public Iterator<Task> getAllTasks() {
    return this.dag.iterator();
  }

  public Iterator<TaskLink> getAllTaskLinks() {
    return this.dag.edgeSet().iterator();
  }

  public TaskLink getTaskLink(Task from, Task to) {
    return this.dag.getEdge(from, to);
  }

  public boolean needFork(Task task) {
    if (task == null) {
      throw new RuntimeException("Input task can not be null");
    }
    if (!this.dag.containsVertex(task)) {
      throw new RuntimeException(
          "Unkown task, ID is " + task.getId() + ", Name is " + task.getName());
    }
    return this.forkTasks.containsKey(task.getId());
  }

  public boolean needJoin(Task task) {
    if (task == null) {
      throw new RuntimeException("Input task can not be null");
    }
    if (!this.dag.containsVertex(task)) {
      throw new RuntimeException(
          "Unkown task, ID is " + task.getId() + ", Name is " + task.getName());
    }
    Set<TaskLink> taskLinks = this.dag.edgesOf(task);
    for (Iterator<TaskLink> it = taskLinks.iterator(); it.hasNext(); ) {
      TaskLink taskLink = it.next();
      if (taskLink.getFrom() == task && this.joinTasks.containsKey(taskLink.getTo().getId())) {
        return true;
      }
    }
    return false;
  }

  public Map<String, Task> forkTasks() {
    if (forkTasks == null) {
      forkTasks = findForkTasks();
    }
    return this.forkTasks;
  }

  public Map<String, Task> joinTasks() {
    if (joinTasks == null) {
      joinTasks = findJoinTasks();
    }
    return this.joinTasks;
  }

  private Map<String, Task> findForkTasks() {
    if (this.dag.vertexSet() == null || this.dag.vertexSet().size() == 0) {
      throw new RuntimeException(
          "Not found any tasks, please check the task flow is valid or not.");
    }
    Map<String, Task> tasks = new HashMap<String, Task>();
    for (Iterator<Task> it = this.dag.iterator(); it.hasNext(); ) {
      Task task = it.next();
      if (this.dag.outgoingEdgesOf(task).size() > 1) {
        tasks.put(task.getId(), task);
      }
    }
    return tasks;
  }

  private Map<String, Task> findJoinTasks() {
    if (this.dag.vertexSet() == null || this.dag.vertexSet().size() == 0) {
      throw new RuntimeException(
          "Not found any tasks, please check the task flow is valid or not.");
    }
    Map<String, Task> tasks = new HashMap<String, Task>();
    for (Iterator<Task> it = this.dag.iterator(); it.hasNext(); ) {
      Task task = it.next();
      if (this.dag.incomingEdgesOf(task).size() > 1) {
        tasks.put(task.getId(), task);
      }
    }
    return tasks;
  }

}
