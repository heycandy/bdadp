package com.chinasofti.ark.bdadp.service.flow.bean;

import com.chinasofti.ark.bdadp.component.api.Component;
import com.chinasofti.ark.bdadp.service.graph.bean.Graph;

/**
 * Created by White on 2016/09/21.
 */
public abstract class Flow implements Component {

    protected abstract Graph getGraph();
}
