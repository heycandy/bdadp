package com.chinasofti.ark.bdadp.service.scenario.bean;

import com.chinasofti.ark.bdadp.component.api.Component;
import com.chinasofti.ark.bdadp.component.api.Listener;
import com.chinasofti.ark.bdadp.component.support.SimpleTask;
import com.chinasofti.ark.bdadp.dao.schedule.ScheduleExecuteHistoryDao;
import com.chinasofti.ark.bdadp.entity.schedule.ScenarioExecuteHistory;
import com.chinasofti.ark.bdadp.service.ServiceContext;
import com.chinasofti.ark.bdadp.service.flow.bean.SimpleCallableFlow;
import com.chinasofti.ark.bdadp.service.push.PushService;
import com.chinasofti.ark.bdadp.service.push.bean.EventBody;
import com.chinasofti.ark.bdadp.util.common.BeanHelper;

import java.util.Date;

/**
 * Created by White on 2016/10/14.
 */
public class ScenarioScheduleListener implements Listener {

    private ScheduleExecuteHistoryDao scheduleExecuteHistoryDao;
    private PushService pushService;

    public ScenarioScheduleListener() {
        this.scheduleExecuteHistoryDao = ServiceContext.getService(ScheduleExecuteHistoryDao.class);
        this.pushService = ServiceContext.getService(PushService.class);

    }

    @SuppressWarnings("rawtypes")
    @Override
    public void listen(Component component) {
        ScenarioExecuteHistory entity;

        if (component instanceof SimpleTask) {
            SimpleTask task = ((SimpleTask) component);

            entity = new ScenarioExecuteHistory(task.getExecutionId(), task.getId());

            entity.setExecuteStatus(task.getState());
            try {
                entity.setExecuteProgress(task.getProgress());
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }

            entity.setScenarioId(task.getScenarioId());
            entity.setTaskName(task.getName());
        } else if (component instanceof SimpleCallableFlow) {
            SimpleCallableFlow flow = ((SimpleCallableFlow) component);

            entity = new ScenarioExecuteHistory(flow.getExecutionId(), flow.getId());

            entity.setExecuteStatus(flow.getState());
            try {
                entity.setExecuteProgress(flow.getProgress());
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
            entity.setScenarioId(flow.getScenarioId());
            entity.setTaskName(flow.getName());
        } else {
            System.err.println("component: " + component.getClass().getName());
            return;
        }

        switch (entity.getExecuteStatus()) {
            case 0:
                entity.setCreateTime(new Date());
                break;
            case 1:
                entity.setStartTime(new Date());
                break;
            default:
                entity.setEndTime(new Date());
                break;
        }

        ScenarioExecuteHistory
                updateEntity =
                scheduleExecuteHistoryDao.findByUnionId(entity.getUnionId());

        if (null != updateEntity) {

            BeanHelper.mergeProperties(entity, updateEntity);
            scheduleExecuteHistoryDao.save(updateEntity);
        } else {
            scheduleExecuteHistoryDao.save(entity);
        }

        pushService.sendEvent(new EventBody(0, "monitor", component));
    }

}
