package com.chinasofti.ark.bdadp.service.queue.impl;

import com.chinasofti.ark.bdadp.service.push.PushService;
import com.chinasofti.ark.bdadp.service.push.bean.EventBody;
import com.chinasofti.ark.bdadp.service.queue.QueueTaskService;
import com.chinasofti.ark.bdadp.service.queue.bean.Previewable;
import com.chinasofti.ark.bdadp.service.queue.bean.QueueTask;
import com.chinasofti.ark.bdadp.service.scenario.bean.ScenarioServiceException;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.common.base.Function;
import com.google.common.collect.Queues;
import com.google.common.collect.Sets;
import java8.util.Comparators;
import java8.util.Optional;
import java8.util.stream.Collectors;
import java8.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Date;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.BlockingQueue;

/**
 * Created by White on 2016/11/17.
 */
@Service
public class QueueTaskServiceImpl implements QueueTaskService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final int MAXIMUM_QUEUE_CAPCITY = 5;

    private BlockingQueue<ProxyQueueTask> readyQueue;
    private BlockingQueue<ProxyQueueTask> completingQueue;
    private Queue<ProxyQueueTask> doneQueue;
    private Set<ProxyQueueTask> allSet;

    private volatile Thread thread;
    private volatile boolean isDestroyed;

    @Autowired
    private PushService pushService;

    @Override
    public boolean submit(QueueTask e) {
        ProxyQueueTask e0 = new ProxyQueueTask(e);
        e0.addListener(f -> {
            pushService.sendEvent(new EventBody(0, "msg", e0));
            return null;
        });
        return this.readyQueue.add(e0) && this.allSet.add(e0);
    }

    @Override
    public Object preview(QueueTask task) {
        try {
            if (task instanceof Previewable) {
                return ((Previewable) task).preview();
            }
        } catch (Exception e) {
            throw new ScenarioServiceException(22999, e.getMessage());
        }

        return null;
    }

    @Override
    public Iterable<QueueTask> findAll() {
        return StreamSupport.stream(this.allSet)
                .sorted(Comparators.comparing(QueueTask::getCreateTime))
                .collect(Collectors.toSet());
    }

    @Override
    public boolean cancel(String id) {
        Optional<ProxyQueueTask> optional = StreamSupport.stream(this.completingQueue)
                .filter(f -> f.getId().equals(id))
                .findFirst();

        if (optional.isPresent()) {
            optional.ifPresent(ProxyQueueTask::cancel);

            return this.completingQueue.remove(optional.get()) &&
                    this.doneQueue.add(optional.get());
        } else {
            optional = StreamSupport.stream(this.readyQueue)
                    .filter(f -> f.getId().equals(id))
                    .findFirst();

            if (optional.isPresent()) {
                optional.ifPresent(ProxyQueueTask::cancel);

                return this.readyQueue.remove(optional.get()) &&
                        this.doneQueue.add(optional.get());
            }
        }

        return false;
    }

    @Override
    public Iterable<String> cancel(Iterable<String> iterable) {
        Collection<String> failure = Sets.newHashSet();
        for (String s : iterable) {
            if (!this.cancel(s)) {
                failure.add(s);
            }
        }

        return failure;
    }

    @Override
    public boolean remove(String id) {
        return this.doneQueue.removeIf(f -> f.getId().equals(id) && f.remove()) &&
                this.allSet.removeIf(f -> f.getId().equals(id));
    }

    @Override
    public Iterable<String> remove(Iterable<String> iterable) {
        Collection<String> failure = Sets.newHashSet();
        for (String s : iterable) {
            if (!this.remove(s)) {
                failure.add(s);
            }
        }

        return failure;
    }

    @Override
    public void destroy() throws Exception {
        this.isDestroyed = true;
        this.thread.interrupt();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.readyQueue = Queues.newLinkedBlockingQueue();
        this.completingQueue = Queues.newLinkedBlockingQueue(MAXIMUM_QUEUE_CAPCITY);
        this.doneQueue = Queues.newConcurrentLinkedQueue();
        this.allSet = Sets.newHashSet();

        this.isDestroyed = false;

        this.thread = new Thread("queue-task-ready-completing") {
            private final Logger logger = LoggerFactory.getLogger(this.getClass());

            @Override
            public void run() {
                while (!isDestroyed) {
                    try {
                        ProxyQueueTask e = readyQueue.take();
                        logger.debug("take task -> " + e);
                        completingQueue.put(e);
                        logger.debug("put task -> " + e);
                        new Thread(e, "queue-task-" + e.getName()).start();
                        logger.debug("start task -> " + e);
                    } catch (InterruptedException e) {
                        logger.warn(this + " -> " + e.getMessage());
                    }
                }
            }
        };

        this.thread.start();
    }

    class ProxyQueueTask implements QueueTask {

        private final QueueTask target;

        private volatile Thread thread;
        private int state;

        public ProxyQueueTask(QueueTask target) {
            this.target = target;
        }

        @Override
        public String getId() {
            return this.target.getId();
        }

        @Override
        public String getName() {
            return this.target.getName();
        }

        @Override
        public String getDesc() {
            return this.target.getDesc();
        }

        @Override
        public String getAction() {
            return this.target.getAction();
        }

        @Override
        public double getProgress() {
            return this.target.getProgress();
        }

        @Override
        public void addListener(Function<QueueTask, Void> e) {
            this.target.addListener(e);
        }

        @Override
        public void reportAll() {
            this.target.reportAll();
        }

        @Override
        public boolean remove() {
            return this.target.remove();
        }

        @Override
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        public Date getCreateTime() {
            return this.target.getCreateTime();
        }

        @Override
        public String getCreateUser() {
            return this.target.getCreateUser();
        }

        public int getState() {
            return this.state;
        }

        private void setState(int state) {
            this.state = state;

            this.reportAll();
        }

        public boolean isDone() {
            return this.state > 1;
        }

        public boolean isCancel() {
            return this.state == 4;
        }

        public void cancel() {
            if (this.thread != null) {
                try {
                    this.thread.interrupt();
                } catch (Exception e) {
                    logger.warn(this + " -> " + e.getMessage());
                }

                this.setState(4);
            }
        }

        @Override
        public void run() {
            try {
                this.thread = Thread.currentThread();
                this.setState(1);
                this.target.run();
                this.setState(2);

            } catch (Exception e) {
                this.setState(3);
                logger.error(this.toString(), e);
            } finally {
                completingQueue.remove(this);
                doneQueue.add(this);
            }

        }

        @Override
        public String toString() {
            return "QueueTask{" +
                    "id=" + this.getId() +
                    ",name=" + this.getName() +
                    ",desc=" + this.getDesc() +
                    ",action=" + this.getAction() +
                    ",progress=" + this.getProgress() +
                    ",createTime=" + this.getCreateTime() +
                    ",createUser=" + this.getCreateUser() +
                    ",state=" + this.getState() +
                    '}';
        }
    }
}
