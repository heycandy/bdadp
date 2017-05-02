package com.chinasofti.ark.bdadp.flow;

import com.chinasofti.ark.bdadp.util.common.Pair;

public class TaskLink {

  private Pair<Task, Task> tuple;

  TaskLink(Task from, Task to) {
    this.tuple = new Pair<Task, Task>(from, to);
  }

  Task getFrom() {
    return this.tuple.getFirst();
  }

  Task getTo() {
    return this.tuple.getSecond();
  }
}
