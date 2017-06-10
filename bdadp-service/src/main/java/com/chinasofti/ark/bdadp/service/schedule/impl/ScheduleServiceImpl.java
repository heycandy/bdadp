package com.chinasofti.ark.bdadp.service.schedule.impl;

import com.chinasofti.ark.bdadp.dao.scenario.ScenarioDao;
import com.chinasofti.ark.bdadp.dao.scenario.ScenarioGraphDagDao;
import com.chinasofti.ark.bdadp.dao.schedule.HolidayDao;
import com.chinasofti.ark.bdadp.dao.schedule.ScheduleDao;
import com.chinasofti.ark.bdadp.dao.schedule.ScheduleExecuteHistoryDao;
import com.chinasofti.ark.bdadp.dao.task.TaskDao;
import com.chinasofti.ark.bdadp.dao.user.UserDao;
import com.chinasofti.ark.bdadp.entity.scenario.Scenario;
import com.chinasofti.ark.bdadp.entity.schedule.Holiday;
import com.chinasofti.ark.bdadp.entity.schedule.ScenarioExecuteHistory;
import com.chinasofti.ark.bdadp.entity.schedule.ScenarioExecuteHistoryPK;
import com.chinasofti.ark.bdadp.entity.schedule.Schedule;
import com.chinasofti.ark.bdadp.service.scenario.bean.ScenarioStatus;
import com.chinasofti.ark.bdadp.service.schedule.ScheduleService;
import com.chinasofti.ark.bdadp.util.common.BeanHelper;
import com.chinasofti.ark.bdadp.util.common.DateUtil;
import com.chinasofti.ark.bdadp.util.constant.Constants;
import com.chinasofti.ark.bdadp.util.dto.MonitorDTO;
import com.chinasofti.ark.bdadp.util.dto.PageDTO;
import com.chinasofti.ark.bdadp.util.dto.ResultDTO;
import com.chinasofti.ark.bdadp.util.dto.ScheduleDTO;
import com.chinasofti.ark.bdadp.util.dto.ScheduleJobRunBean;
import com.chinasofti.ark.bdadp.util.schedule.ScheduleUtils;

import org.apache.commons.lang.StringUtils;
import org.quartz.CronExpression;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author : water
 * @Date : 2016年9月9日
 * @Desc : 任务调度serviceImpl
 * @version: V1.0
 */
@Component
@Transactional
public class ScheduleServiceImpl implements ScheduleService {

    private final static String simpleDateFormat = "yyyy-MM-dd HH:mm:ss";
    private final Logger LOG = LoggerFactory.getLogger(ScheduleService.class);
    @Autowired
    private ScenarioDao scenarioDao;

    @Autowired
    private HolidayDao holidayDao;


    @Autowired
    private ScheduleDao scheduleDao;

    @Autowired
    private TaskDao taskDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private ScheduleExecuteHistoryDao scheduleExecuteHistoryDao;

    @Autowired
    private ScenarioGraphDagDao scenarioGraphDagDao;

    public ResultDTO addSchedule(ScheduleDTO dto) {
        ResultDTO resultDTO = new ResultDTO();
        String cronExpression;
        if (dto.getTriggerType().equalsIgnoreCase(Constants.QZ_CRON_TRIGGER)) {
            cronExpression = dto.getCronExpression();
            if (!CronExpression.isValidExpression(cronExpression)) {
                resultDTO.setResultMessage(cronExpression + " is wrong!!!!");
                resultDTO.setResultCode(1);
                return resultDTO;
            }
        } else {
            cronExpression = ScheduleUtils.createCronExpression(dto);
            dto.setCronExpression(cronExpression);
        }
        Schedule findOne = scheduleDao.findOne(dto.getScenarioId());
        if (null != findOne) {
            scheduleDao.delete(findOne);
        }
        Schedule schedule = new Schedule();
        BeanHelper.copyProperties(dto, schedule);
        schedule.setCreateTime(new Date());
        scheduleDao.save(schedule);
        resultDTO.setResultCode(0);
        return resultDTO;
    }

    public PageDTO getPageJobs(Integer jobStatus, Integer dayNum, String search, Pageable page) {
        PageDTO result = new PageDTO();
        Date searchDay = ScheduleUtils.getSearchDay(dayNum);
        Page<MonitorDTO> resultPage = null;

        boolean flagJobStatus = jobStatus != null && jobStatus > 0;
        boolean flagSearch = StringUtils.isBlank(search);
        if (flagJobStatus) {
            if (flagSearch) {
                resultPage =
                        scheduleExecuteHistoryDao.getPageJobByStatusAndSearchday(page, jobStatus, searchDay);
            } else {
                List<String> scenarioIds = scenarioDao.findByScenarioNameLike(search);
                resultPage =
                        scheduleExecuteHistoryDao.getPageJobsByStatusAndSearchdayAndScenarioIds(page, jobStatus,
                                searchDay,
                                scenarioIds);
            }
        } else {
            if (flagSearch) {
                resultPage = scheduleExecuteHistoryDao.getPageJobsBySearchday(page, searchDay);
            } else {
                List<String> scenarioIds = scenarioDao.findByScenarioNameLike(search);
                if (scenarioIds.size() > 0) {
                    resultPage =
                            scheduleExecuteHistoryDao.getPageJobsBySearchdayAndScenarioIds(page, searchDay,
                                    scenarioIds);
                } else {
                    result.setTotal(0L);
                    result.setRows(new ArrayList<MonitorDTO>());
                    return result;
                }
            }
        }
        resultPage = transform(resultPage);
        result.setTotal(resultPage.getTotalElements());
        result.setRows(resultPage.getContent());
        return result;
    }

    public PageDTO getPageTasks(String executionId, String scenarioId, Pageable page) {
        PageDTO result = new PageDTO();
        Page<MonitorDTO>
                resultPage =
                scheduleExecuteHistoryDao.getPageTasks(page, executionId, scenarioId);
        resultPage = transform(resultPage);
        String graphRaw = scenarioGraphDagDao.findGraphRawByScenarioId(scenarioId);
        result.setGraphRaw(graphRaw);
        result.setTotal(resultPage.getTotalElements());
        result.setRows(resultPage.getContent());
        return result;
    }

    public Page<MonitorDTO> transform(Page<MonitorDTO> resultPage) {
        for (MonitorDTO monitorDTO : resultPage) {
            String scenarioId = monitorDTO.getScenarioId();
//            String taskId = monitorDTO.getTaskId();
            Date startTime;
          if (monitorDTO.getDateCreateTime() != null) {
                startTime = monitorDTO.getDateCreateTime();
            } else {
                startTime = monitorDTO.getDateCommitTime();
            }
            Date endTime = monitorDTO.getEndTime();
            String runTime = ScheduleUtils.getRunTime(startTime, endTime);
            String status = ScheduleUtils.statusIntToString(monitorDTO.getIntStatus());
//            Task task = taskDao.findOne(taskId);
//			if (null == task) {
//				monitorDTO.setTaskName("taskName为空");
//			} else {
//				monitorDTO.setTaskName(task.getTaskName());
//			}
            String scenarioName = scenarioDao.findOne(scenarioId).getScenarioName();
            Schedule schedule = scheduleDao.findOne(scenarioId);
            if (null == schedule) {
              monitorDTO.setUserName("-");
            } else {
                monitorDTO.setUserName(userDao.findOne(schedule.getUserId()).getUserName());
            }
            monitorDTO.setScenarioName(scenarioName);
            monitorDTO.setStatus(status);
            monitorDTO.setRunTime(runTime);
            monitorDTO.setCommitTime(DateUtil.getDateTime(simpleDateFormat, startTime));
        }
        return resultPage;
    }

    public void changeLineStatus(String scenarioId, String userId, int changeStatus) {
        Scenario s = scenarioDao.findOne(scenarioId);
        s.setScenarioStatus(changeStatus);
        if (changeStatus == ScenarioStatus.ONLINE.ordinal()) {
            s.setOnlineTime(new Date());
            s.setOnlineUser(userId);
        } else if (changeStatus == ScenarioStatus.OFFLINE.ordinal()) {
            s.setOfflineTime(new Date());
            s.setOfflineUser(userId);
        }
        scenarioDao.save(s);
    }

    public ResultDTO offlineSchedule(ScheduleJobRunBean[] arr) {
        ResultDTO resultDTO = new ResultDTO();
        if (null == arr || arr.length <= 0) {
            return null;
        }
        for (ScheduleJobRunBean bean : arr) {
            String scenarioId = bean.getScenarioId();
            String jobGroup = bean.getJobGroup();
            String userId = bean.getUserId();
            ScheduleUtils.stopSchedule(scenarioId, jobGroup);
            changeLineStatus(scenarioId, userId, ScenarioStatus.OFFLINE.ordinal());
        }
        resultDTO.setResultCode(Constants.RESULT_SUCCESS_CODE);
        resultDTO.setResultMessage(Constants.RESULT_SUCCESS_MSG);
        return resultDTO;
    }

    /**
     * 新的上线接口
     */
    public List<ScheduleJobRunBean> executeSchedules(ScheduleJobRunBean[] list) {

        List<ScheduleJobRunBean> result = new ArrayList<ScheduleJobRunBean>();
        if (null == list || list.length <= 0) {
            result = null;
        }
        try {
            Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
            for (ScheduleJobRunBean bean : list) {
                String scenarioId = bean.getScenarioId();
                String jobGroup = bean.getJobGroup();
                String userId = bean.getUserId();
                JobKey jobKey = ScheduleUtils.getJobKey(scenarioId, jobGroup);
                scheduler.deleteJob(jobKey);
                Schedule schedule = scheduleDao.findOne(scenarioId);

                if (null == schedule) {
//					result = nullTriggerScenarios(scenarioId, jobGroup);
                    ScheduleJobRunBean beanScheduleJobRunBean = new ScheduleJobRunBean();
                    beanScheduleJobRunBean.setJobGroup(jobGroup);
                    beanScheduleJobRunBean.setScenarioId(scenarioId);
                    result.add(beanScheduleJobRunBean);
                } else {
                    executeSchedule(scenarioId, userId, jobGroup);
                }
            }
        } catch (SchedulerException e) {
            e.printStackTrace();
            LOG.error("运行一次定时任务失败: " + e.getMessage());
        }
        return result;
    }

    public List<ScheduleJobRunBean> nullTriggerScenarios(String scenarioId, String jobGroup) {
        List<ScheduleJobRunBean> result = new ArrayList<ScheduleJobRunBean>();
        ScheduleJobRunBean bean = new ScheduleJobRunBean();
        bean.setJobGroup(jobGroup);
        bean.setScenarioId(scenarioId);
        result.add(bean);
        return result;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public void executeSchedule(String scenarioId, String userId, String jobGroup) {
        Scheduler scheduler;
        try {
            scheduler = StdSchedulerFactory.getDefaultScheduler();
            JobKey jobKey = ScheduleUtils.getJobKey(scenarioId, jobGroup);
            scheduler.deleteJob(jobKey);
            Schedule schedule = scheduleDao.findOne(scenarioId);

            changeLineStatus(scenarioId, userId, ScenarioStatus.ONLINE.ordinal());

            Map<String, Object> jobDataMap = new HashMap<String, Object>();
            TriggerBuilder triggerBuilder = ScheduleUtils.createTriggerBuilder(scenarioId, jobGroup);
            JobBuilder
                    jobBuilder =
                    ScheduleUtils.createJobBuilder(MyFactoryJob.class, scenarioId, jobGroup);
            JobDetail jobDetail = ScheduleUtils.createJobDetail(jobBuilder, jobDataMap);
            jobDetail.getJobDataMap().put("scenarioId", schedule.getScenarioId());
            String triggerType = schedule.getTriggerType();
            if (triggerType.equalsIgnoreCase(Constants.QZ_CRON_TRIGGER)) {

                String cronExpression = schedule.getCronExpression();
                CronTrigger cronTrigger = ScheduleUtils.createCronTrigger(triggerBuilder, cronExpression);
                scheduler.scheduleJob(jobDetail, cronTrigger);

            } else if (triggerType.equalsIgnoreCase(Constants.QZ_SIMPLE_TRIGGER)) {
                Date
                        startTime =
                        ScheduleUtils.mergeRunTime(schedule.getStartTime(), schedule.getExecutionTime());
                Date endTime = schedule.getEndTime();
                int repeatInterval = schedule.getRepeatInterval();
                String cronExpression = schedule.getCronExpression();
                CronTrigger
                        cronTrigger =
                        ScheduleUtils.createCronTrigger(triggerBuilder, startTime, endTime,
                                cronExpression);
                if (ScheduleUtils.isExecute(repeatInterval, schedule.getExecutionFrequencyUnit(),
                        schedule.getExecutionTime())) {
                    scheduler.scheduleJob(jobDetail, cronTrigger);
                }
            }
            if (!isHoliDay()) {
                scheduler.start();
            }
        } catch (SchedulerException e) {
            e.printStackTrace();
        }

    }

    public Boolean isHoliDay() {
        Boolean isHoliday = true;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Holiday d = holidayDao.findByHolidayValue(sdf.format(new Date()));
        if (null == d) {
            isHoliday = false;
        }
        return isHoliday;
    }

    public void pauseJob(JobKey jobKey) {
        try {
            Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
            scheduler.pauseJob(jobKey);
            LOG.info("Pause scene successful: " + jobKey.getName());
        } catch (SchedulerException e) {
            e.printStackTrace();
            LOG.error("暂停定时任务失败: " + e.getMessage());
        }
    }

    public void resumeJob(JobKey jobKey) {
        try {
            Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
            scheduler.resumeJob(jobKey);
            LOG.info("Resume scene successful: " + jobKey.getName());
        } catch (SchedulerException e) {
            e.printStackTrace();
            LOG.error("恢复定时任务失败: " + e.getMessage());
        }
    }

    public ScheduleDTO getScheduleInfo(String scenarioId) {

        ScheduleDTO result = new ScheduleDTO();
        Schedule schedule = scheduleDao.findOne(scenarioId);
        if (null != schedule) {
            BeanHelper.copyProperties(schedule, result);
        } else {
            return null;
        }
        if (schedule.getTriggerType().equalsIgnoreCase(Constants.QZ_SIMPLE_TRIGGER)) {
            result.setStartTimeStr(DateUtil.convertTimeToString(result.getStartTime()));
            if (result.getEndTime() != null) {
                result.setEndTimeStr(DateUtil.convertTimeToString(result.getEndTime()));
            } else {
                result.setEndTimeStr(null);
            }

        }
        return result;

    }

    public ScheduleDTO getTasksByScenario(String executionId, String taskId) {

        ScheduleDTO result = new ScheduleDTO();
        ScenarioExecuteHistoryPK unionId = new ScenarioExecuteHistoryPK();
        unionId.setExecutionId(executionId);
        unionId.setTaskId(taskId);
        ScenarioExecuteHistory
                scenarioExecuteHistory =
                scheduleExecuteHistoryDao.findByUnionId(unionId);
        if (null != scenarioExecuteHistory) {
            BeanHelper.copyProperties(scenarioExecuteHistory, result);
            String scenarioId = result.getScenarioId();
            Schedule schedule = scheduleDao.findOne(scenarioId);
            result.setUserName(userDao.findOne(schedule.getUserId()).getUserName());
            result.setScenarioName(scenarioDao.findOne(scenarioId).getScenarioName());
            result.setStatus(scenarioExecuteHistory.getExecuteStatus());
        } else {
            return null;
        }
        return result;

    }
}
