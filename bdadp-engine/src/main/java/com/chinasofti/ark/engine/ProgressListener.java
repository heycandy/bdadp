package com.chinasofti.ark.engine;

public abstract class ProgressListener {

  protected double progress = 0.0;

  public double progress() {
    return this.progress;
  }

  public abstract void onChange();

  public abstract void onStart();

  public abstract void onEnd();

  public abstract void onFailed();

  public abstract void onStop();

}
