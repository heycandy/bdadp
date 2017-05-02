package com.chinasofti.ark.bdadp.service.schedule.impl;

import com.chinasofti.ark.bdadp.dao.schedule.ScheduleHistoryDao;
import com.chinasofti.ark.bdadp.entity.schedule.ScheduleHistory;
import com.chinasofti.ark.bdadp.service.ServiceContext;
import com.chinasofti.ark.bdadp.service.scenario.ScenarioExecutorService;
import com.chinasofti.ark.bdadp.util.common.UUID;
import com.chinasofti.ark.bdadp.util.constant.Constants;
import com.chinasofti.ark.bdadp.util.date.DateTimeUtil;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Calendar;
import java.util.Date;

/**
 * 实现Job接口，定义具体运行的任务
 */
public class MyFactoryJob implements Job {

    private final Logger logger = LoggerFactory.getLogger(MyFactoryJob.class);
    private ScenarioExecutorService
            scenarioExecutorService =
            ServiceContext.getService(ScenarioExecutorService.class);

    private ScheduleHistoryDao
            scheduleHistoryDao =
            ServiceContext.getService(ScheduleHistoryDao.class);

    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        ScheduleHistory bean = new ScheduleHistory();
        bean.setStartTime(new Date());
        String jobName = jobExecutionContext.getJobDetail().getKey().getName();
        JobDataMap dataMap = jobExecutionContext.getJobDetail().getJobDataMap();
        String scenarioId = dataMap.getString("scenarioId");

        try {
            scenarioExecutorService.schedule(scenarioId);
            bean.setScheduleStatus(Constants.RESULT_SUCCESS_MSG);

        } catch (Exception e) {
            bean.setScheduleStatus(Constants.RESULT_EXCEPTION_MSG);
            e.printStackTrace();
        }
        bean.setSchedulehistoryId(UUID.getId());
        bean.setScenarioId(scenarioId);
        bean.setEndTime(new Date());
        scheduleHistoryDao.save(bean);
        String
                jobRunTime =
                DateTimeUtil.format(Calendar.getInstance().getTime(), "yyyy-MM-dd hh:mm:ss.SSS");
        logger.info(" jobName : {} run  jobRunTime : {} ", jobName, jobRunTime);
    }

}
