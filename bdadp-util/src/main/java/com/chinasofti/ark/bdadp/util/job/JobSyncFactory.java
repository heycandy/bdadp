package com.chinasofti.ark.bdadp.util.job;


import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 同步的任务工厂类
 *
 * @author Beacher Han(dirk_han@126.com)
 * @since 2016/3/31
 */
@DisallowConcurrentExecution
public class JobSyncFactory implements Job {


    private static final Logger LOG = LoggerFactory.getLogger(JobSyncFactory.class);

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        try {
            LOG.info("JobSyncFactory execute");
            JobDataMap mergedJobDataMap = jobExecutionContext.getMergedJobDataMap();
//			ScheduleJob scheduleJob = (ScheduleJob) mergedJobDataMap.get(Constants.JOB_PARAM_KEY);
//			System.out.println("jobName:" + scheduleJob.getJobName() + "  " + scheduleJob);
//			ScenesService scenesService = ContextLoader.getCurrentWebApplicationContext().getBean(ScenesService.class);
//			SceneExecTraceEntity sceneExecTrace = new SceneExecTraceEntity();
//			sceneExecTrace.setSceneId(scheduleJob.getJobId());
//			sceneExecTrace.setExecBatchNo(UUID.getId());
//			sceneExecTrace.setSceneExecStatus(Constants.EXEC_STATUS_RUNNING);
//			ScheduleJobService scheService = ContextLoader.getCurrentWebApplicationContext()
//					.getBean(ScheduleJobService.class);
//			scenesService.runScene(sceneExecTrace, scheService);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            LOG.error("Exception when executing quartz job: ", e);
            throw new JobExecutionException(e);
        }
    }
}
