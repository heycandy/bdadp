package com.chinasofti.ark.bdadp.service.graph.bean;

/**
 * Created by White on 2016/09/16.
 */
public interface Vertex {

    String getId();

  boolean isSkip();

  void setSkip(boolean skip);

    int getState();

    void setState(String name);

}
