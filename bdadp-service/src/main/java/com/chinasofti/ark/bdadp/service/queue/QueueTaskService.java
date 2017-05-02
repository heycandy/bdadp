package com.chinasofti.ark.bdadp.service.queue;

import com.chinasofti.ark.bdadp.service.queue.bean.QueueTask;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

/**
 * Created by White on 2016/11/17.
 */
public interface QueueTaskService extends InitializingBean, DisposableBean {

    boolean submit(QueueTask e);

    Object preview(QueueTask task);

    Iterable<QueueTask> findAll();

    boolean cancel(String id);

    Iterable<String> cancel(Iterable<String> iterable);

    boolean remove(String id);

    Iterable<String> remove(Iterable<String> iterable);

}
