package com.chinasofti.ark.bdadp.service.flow.bean;

import com.chinasofti.ark.bdadp.service.flow.FlowExecutorService;

/**
 * Created by White on 2016/09/21.
 */
public interface CallbackExecutor {

    void call(FlowExecutorService executor);
}
