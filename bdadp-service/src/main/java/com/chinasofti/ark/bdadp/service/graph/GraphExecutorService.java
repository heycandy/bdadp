package com.chinasofti.ark.bdadp.service.graph;

import com.chinasofti.ark.bdadp.service.graph.bean.Graph;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

/**
 * Created by White on 2016/09/23.
 */
public interface GraphExecutorService extends InitializingBean, DisposableBean {

    void submit(Graph graph);
}
