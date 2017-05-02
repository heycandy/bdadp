package com.chinasofti.ark.bdadp.util.schedule;

import com.chinasofti.ark.bdadp.util.common.DateUtil;
import com.chinasofti.ark.bdadp.util.date.DateTimeUtil;
import com.chinasofti.ark.bdadp.util.dto.ScheduleDTO;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

/**
 * @Author : water
 * @Date : 2016年9月11日
 * @Desc : 任务调度辅助类
 * @version: V1.0
 */
public class ScheduleUtils {

    public static TriggerKey getTriggerKey(String jobName, String jobGroup) {

        return TriggerKey.triggerKey(jobName, jobGroup);
    }

    public static String statusIntToString(int status) {
        String strStatus;
        switch (status) {
            case 0:
                strStatus = "READY";
                break;
            case 1:
                strStatus = "RUNNING";
                break;
            case 2:
                strStatus = "SUCCESS";
                break;
            default:
                strStatus = "FAILURE";
                break;
        }
        return strStatus;
    }

    public static String getRunTime(Date startTime, Date endTime) {
        long runTime;
        String result;
        if (null == startTime) {
            runTime = 0;
        } else if (null == endTime) {
            runTime = new Date().getTime() - startTime.getTime();
        } else {
            runTime = endTime.getTime() - startTime.getTime();
        }

        if (runTime <= 1000) {
            result = "<1s";
        } else {
            result = DateTimeUtil.formatDateTime(runTime / 1000);
        }
        return result;
    }

    public static Date getSearchDay(Integer dayNum) {
        Date searchDay = null;
        if (null == dayNum || dayNum <= 0) {
            searchDay = new Date(0);
        } else {
            searchDay = DateUtil.getSomeDay(-dayNum);
        }
        return searchDay;
    }

    public static String createCronExpression(ScheduleDTO dto) {

        int executionFrequencyValue = dto.getRepeatInterval();
        String executionFrequencyUnit = dto.getExecutionFrequencyUnit();
        String executionDay = dto.getExecutionDay();
        String executionTime = dto.getExecutionTime();
        String[] result = executionTime.split(":");
        executionTime = convertTimeToCron(executionTime);

        String cronExpression = null;
        switch (executionFrequencyUnit.toLowerCase()) {
            case "hour": // 21 32 0/3 * * ?
                cronExpression = result[2] + " " + result[1] + " 0/" + executionFrequencyValue + " * * ? ";
                break;
            case "day": // 21 32 14 1/3 * ?
                cronExpression = executionTime + " 1/" + executionFrequencyValue + " * ?";
                break;
            case "week": // 21 32 14 ? * 2,4
                cronExpression = executionTime + " ? * " + executionDay;
                break;
            case "month": // 20 11 14 5,21,23 * ?
                cronExpression = executionTime + " " + executionDay + " * ?";
                break;
            default:
                break;
        }

        return cronExpression;
    }

    public static String convertTimeToCron(String executionValue) {
        String[] result = executionValue.split(":");
        return result[2] + " " + result[1] + " " + result[0];

    }

    public static SimpleTrigger createSimpleTrigger(TriggerBuilder<SimpleTrigger> triggerBuilder,
                                                    Date startTime,
                                                    Date endTime, int repeatInterval) {

        SimpleTrigger simpleTrigger = (SimpleTrigger) triggerBuilder.startAt(startTime)
                .withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(repeatInterval))
                .endAt(endTime).build();

        return simpleTrigger;
    }

    public static CronTrigger createCronTrigger(TriggerBuilder<CronTrigger> triggerBuilder,
                                                String cronExpression) {

        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression);
        CronTrigger cronTrigger = (CronTrigger) triggerBuilder
                .withSchedule(scheduleBuilder.withMisfireHandlingInstructionFireAndProceed()).build();

        return cronTrigger;
    }

    public static boolean isExecute(int executionFrequencyValue, String executionFrequencyUnit,
                                    String executionTime) {
        boolean falg = true;
        int current = getCurrentTimeNum(new Date(), executionFrequencyUnit);
        int start = getCurrentTimeNum(new Date(), executionFrequencyUnit);
        if ("week".equalsIgnoreCase(executionFrequencyUnit) || "month"
                .equalsIgnoreCase(executionFrequencyUnit)) {
            if ((current - start) % executionFrequencyValue != 0) { // 不执行
                falg = false;
            }
        }

        return falg;
    }

    // public static void main(String[] args) {
    //// String ss = "21 32 14 0 0 2,4"; //
    //// System.out.println(CronExpression.isValidExpression(ss));
    ////
    //// System.out.println(mergeRunTime(new Date(), "10:25:20"));
    //
    //// String s = "2016-10-10 23:59:59";
    //// DateUtil.
    // }

    public static Date mergeRunTime(Date startTime, String executionTime) {
        String fm = "yyyy-MM-dd HH:mm:ss";
        String strTime = DateTimeUtil.format(startTime, fm);
        String sTime = strTime.substring(0, strTime.indexOf(" ")) + " " + executionTime;
        System.out.println(sTime);
        Date result = null;
        try {
            result = DateUtil.convertStringToDate(fm, sTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;

    }

    public static CronTrigger createCronTrigger(TriggerBuilder<CronTrigger> triggerBuilder,
                                                Date startTime,
                                                Date endTime, String cronExpression) {
        CronTrigger cronTrigger;
        if (null != endTime) {
            cronTrigger = (CronTrigger) triggerBuilder.startAt(startTime).endAt(endTime).withSchedule(
                    (CronScheduleBuilder.cronSchedule(cronExpression))
                            .withMisfireHandlingInstructionDoNothing())
                    .build();
        } else {
            cronTrigger = createCronTrigger(triggerBuilder, startTime, cronExpression);
        }
        return cronTrigger;
    }

    public static CronTrigger createCronTrigger(TriggerBuilder<CronTrigger> triggerBuilder,
                                                Date startTime,
                                                String cronExpression) {

        CronTrigger cronTrigger = (CronTrigger) triggerBuilder.startAt(startTime).withSchedule(
                (CronScheduleBuilder.cronSchedule(cronExpression))
                        .withMisfireHandlingInstructionDoNothing())
                .build();

        return cronTrigger;
    }


    public static void stopSchedule(String scenarioId, String jobGroup) {
        Scheduler scheduler = null;
        try {
            scheduler = StdSchedulerFactory.getDefaultScheduler();
            JobKey jobKey = JobKey.jobKey(scenarioId, jobGroup);
            scheduler.interrupt(jobKey);
            scheduler.deleteJob(jobKey);
        } catch (SchedulerException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public static int getCurrentTimeNum(Date date, String executionFrequencyUnit) {

        int result = 0;
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        switch (executionFrequencyUnit.toLowerCase()) {
            case "hour":
                result = c.get(Calendar.HOUR_OF_DAY);
                break;
            case "day":
                result = c.get(Calendar.DAY_OF_YEAR);
                break;
            case "week":
                result = c.get(Calendar.WEEK_OF_YEAR);
                break;
            default:
                result = c.get(Calendar.MONTH) + 1;
                break;
        }
        return result;
    }

    public static JobDetail createJobDetail(JobBuilder jobBuilder, Map<String, Object> dataMap) {
        JobDetail jobDetail = jobBuilder.requestRecovery(true).storeDurably(true).build();
        for (String key : dataMap.keySet()) {
            jobDetail.getJobDataMap().put(key, dataMap.get(key));
        }
        return jobDetail;
    }

    public static JobBuilder createJobBuilder(Class<? extends Job> jobClass, String jobName,
                                              String jobGroup) {
        return JobBuilder.newJob(jobClass).withIdentity(jobName, jobGroup);
    }

    @SuppressWarnings("rawtypes")
    public static TriggerBuilder createTriggerBuilder(String jobName, String jobGroup) {
        return TriggerBuilder.newTrigger().withIdentity(jobName, jobGroup);
    }

    public static JobKey getJobKey(String jobName, String jobGroup) {

        return JobKey.jobKey(jobName, jobGroup);
    }

}
