package com.chinasofti.ark.bdadp.service.schedule;

import com.chinasofti.ark.bdadp.util.dto.PageDTO;
import com.chinasofti.ark.bdadp.util.dto.ResultDTO;
import com.chinasofti.ark.bdadp.util.dto.ScheduleDTO;
import com.chinasofti.ark.bdadp.util.dto.ScheduleJobRunBean;
import org.quartz.JobKey;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @Author : water
 * @Date : 2016年9月19日
 * @Desc : TODO
 * @version: V1.0
 */

public interface ScheduleService {

    public ResultDTO addSchedule(ScheduleDTO scheduleDTO);

    public PageDTO getPageJobs(Integer jobStatus, Integer dayNum, String search, Pageable page);

    public PageDTO getPageTasks(String executionId, String scenarioId, Pageable page);

    public ResultDTO offlineSchedule(ScheduleJobRunBean[] arr);

    public List<ScheduleJobRunBean> executeSchedules(ScheduleJobRunBean[] list);

    public void pauseJob(JobKey jobKey);

    public void resumeJob(JobKey jobKey);

    public ScheduleDTO getScheduleInfo(String scenarioId);

    public ScheduleDTO getTasksByScenario(String executionId, String taskId);

}
