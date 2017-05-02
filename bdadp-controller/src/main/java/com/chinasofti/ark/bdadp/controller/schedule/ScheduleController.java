package com.chinasofti.ark.bdadp.controller.schedule;

import com.chinasofti.ark.bdadp.controller.bean.ResultBody;
import com.chinasofti.ark.bdadp.service.schedule.ScheduleService;
import com.chinasofti.ark.bdadp.util.common.PageUtil;
import com.chinasofti.ark.bdadp.util.dto.PageDTO;
import com.chinasofti.ark.bdadp.util.dto.ResultDTO;
import com.chinasofti.ark.bdadp.util.dto.ScheduleDTO;
import com.chinasofti.ark.bdadp.util.dto.ScheduleJobRunBean;
import com.chinasofti.ark.bdadp.util.exeption.ScheduleException;
import org.quartz.JobKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author : water
 * @Date : 2016年9月11日
 * @Desc : TODO
 * @version: V1.0
 */

@RestController
@RequestMapping(value = "/service/v1/schedule")
public class ScheduleController {

    private static final Logger logger = LoggerFactory.getLogger(ScheduleController.class);

    @Resource
    private ScheduleService scheduleService;

    /**
     * 添加job
     */
    @SuppressWarnings("rawtypes")
    @RequestMapping(value = "/add", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResultBody addSchedule(@RequestBody ScheduleDTO scheduleDTO) throws ScheduleException {

        ResultBody<ResultDTO> body = new ResultBody<ResultDTO>();
        if (logger.isDebugEnabled()) {
            logger.debug("add Schedule trigger success with  ScenarioId : {} and jobGroup : {}",
                    scheduleDTO.getScenarioId(), scheduleDTO.getJobGroup());
        }
        ResultDTO result = scheduleService.addSchedule(scheduleDTO);
        if (result == null || result.getResultCode() == 1) {
            body.setResultCode(result.getResultCode());
            body.setResultMessage(result.getResultMessage() + "");
            body.setResult(result);
        } else {
            body.setResultCode(0);
            body.setResultMessage("添加成功");
            body.setResult(result);
        }
        return body;
    }

    /**
     * delete job
     */
    @SuppressWarnings("rawtypes")
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResultBody offlineSchedule(@RequestBody ScheduleJobRunBean[] list)
            throws ScheduleException {
        ResultBody<ResultDTO> body = new ResultBody<ResultDTO>();
        if (logger.isDebugEnabled()) {
            logger.debug("delete schedule success");
        }
        ResultDTO result = scheduleService.offlineSchedule(list);
        body.setResultCode(0);
        body.setResultMessage("下线成功");
        body.setResult(result);

        return body;
    }

    @RequestMapping(value = "/pagejobs", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResultBody<PageDTO> getPageJobs(
            @RequestParam(value = "jobStatus", required = false) Integer jobStatus,
            @RequestParam(value = "dayNum", required = false) Integer dayNum,
            @RequestParam(value = "search", required = false) String search,
            @RequestParam(value = "offset", required = false) Integer offset,
            @RequestParam(value = "limit", required = false) Integer limit) throws Exception {
        if (logger.isDebugEnabled()) {
            logger.debug(
                    "find scenarios with jobStatus: {} and dayNum : {}  and search : {} and offset : {} and limit : {} ",
                    jobStatus, dayNum, search, offset, limit);
        }

        ResultBody<PageDTO> body = new ResultBody<PageDTO>();

        Pageable page = PageUtil.createPageRequest(offset, limit);
        PageDTO result = scheduleService.getPageJobs(jobStatus, dayNum, search, page);
        body.setResultCode(0);
        body.setResultMessage("查询成功");
        body.setResult(result);
        return body;
    }

    @RequestMapping(value = "/pagetasks", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResultBody<PageDTO> getPageTasks(
            @RequestParam(value = "executionId", required = true) String executionId,
            @RequestParam(value = "scenarioId", required = true) String scenarioId,
            @RequestParam(value = "offset", required = false) Integer offset,
            @RequestParam(value = "limit", required = false) Integer limit) throws Exception {
        if (logger.isDebugEnabled()) {
            logger.debug(
                    "find tasks with executionId: {} and scenarioId : {}   and offset : {} and limit : {} ",
                    executionId, scenarioId, offset, limit);
        }
        ResultBody<PageDTO> body = new ResultBody<PageDTO>();

        Pageable page = PageUtil.createPageRequest(offset, limit);
        PageDTO result = scheduleService.getPageTasks(executionId, scenarioId, page);
        body.setResultCode(0);
        body.setResultMessage("查询成功");
        body.setResult(result);
        return body;
    }

    @SuppressWarnings("rawtypes")
    @RequestMapping(value = "/run", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResultBody runSchedules(@RequestBody ScheduleJobRunBean[] list) throws ScheduleException {
        ResultBody<List<ScheduleJobRunBean>> body = new ResultBody<List<ScheduleJobRunBean>>();

        if (logger.isDebugEnabled()) {
            logger.debug("online scenarioSchedule list");
        }

        List<ScheduleJobRunBean> result = scheduleService.executeSchedules(list);
        body.setResultCode(0);
        body.setResultMessage("上线成功");
        body.setResult(result);
        return body;
    }

    /**
     * 调度---点击场景获取该场景的调度属性
     */
    @SuppressWarnings("rawtypes")
    @RequestMapping(value = "/ScheduleInfo", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResultBody getScheduleInfo(
            @RequestParam(value = "scenarioId", required = true) String scenarioId,
            @RequestParam(value = "jobGroup", required = true) String jobGroup) throws ScheduleException {
        ResultBody<ScheduleDTO> body = new ResultBody<ScheduleDTO>();
        if (logger.isDebugEnabled()) {
            logger
                    .debug("find scenarioSchedule info  with  scenarioId : {} and jobGroup : {}", scenarioId,
                            jobGroup);
        }
        ScheduleDTO result = scheduleService.getScheduleInfo(scenarioId);
        body.setResultCode(0);
        body.setResultMessage("查询成功");
        body.setResult(result);
        return body;
    }

    /**
     * 监控 ---- 点击场景，获取该场景的tasks
     */
    @SuppressWarnings("rawtypes")
    @RequestMapping(value = "/tasks", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResultBody getTasksByScenario(
            @RequestParam(value = "executionId", required = true) String executionId,
            @RequestParam(value = "taskId", required = true) String taskId) throws ScheduleException {
        ResultBody<ScheduleDTO> body = new ResultBody<ScheduleDTO>();
        if (logger.isDebugEnabled()) {
            logger.debug("find one scenarioSchedule tasks info  with  executionId : {} and taskId : {}",
                    executionId,
                    taskId);
        }
        ScheduleDTO result = new ScheduleDTO();

        result = scheduleService.getTasksByScenario(executionId, taskId);
        body.setResultCode(0);
        body.setResultMessage("查询成功");
        body.setResult(result);
        return body;
    }

    /**
     * 暂停定时任务
     */
    @RequestMapping(value = "/pauseJob", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> pauseJob(
            @RequestParam(value = "jobName", required = true) String jobName,
            @RequestParam(value = "jobGroup", required = true) String jobGroup) throws ScheduleException {

        if (logger.isDebugEnabled()) {
            logger
                    .debug("pause scenarioSchedule  with  jobName : {} and jobGroup : {}", jobName, jobGroup);
        }
        JobKey jobKey = JobKey.jobKey(jobName, jobGroup);
        scheduleService.pauseJob(jobKey);
        return new ResponseEntity<Object>("success", HttpStatus.OK);
    }

    /**
     * 恢复定时任务
     */
    @RequestMapping(value = "/resumeJob", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> resumeJob(
            @RequestParam(value = "jobName", required = true) String jobName,
            @RequestParam(value = "jobGroup", required = true) String jobGroup) throws ScheduleException {

        if (logger.isDebugEnabled()) {
            logger.debug("resume schedule  with  jobName : {} and jobGroup : {}", jobName, jobGroup);
        }
        JobKey jobKey = JobKey.jobKey(jobName, jobGroup);
        scheduleService.resumeJob(jobKey);
        return new ResponseEntity<Object>("success", HttpStatus.OK);
    }

}
