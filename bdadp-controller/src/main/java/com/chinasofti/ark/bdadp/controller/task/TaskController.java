package com.chinasofti.ark.bdadp.controller.task;

import com.chinasofti.ark.bdadp.controller.bean.ResultBody;
import com.chinasofti.ark.bdadp.entity.scenario.ScenarioVersion;
import com.chinasofti.ark.bdadp.entity.task.Task;
import com.chinasofti.ark.bdadp.service.scenario.ScenarioVersionService;
import com.chinasofti.ark.bdadp.service.task.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by TongTong on 2016/08/29.
 */
@Controller
@RequestMapping(value = "/service/v1")
public class TaskController {

    @Autowired
    private TaskService taskService;
    @Autowired
    private ScenarioVersionService scenarioVersionService;
    /**
     * 记录日志
     */
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 作业创建
     */
    @RequestMapping(value = "/scenario/{scenario_id}/task", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public ResultBody createTask(@PathVariable("scenario_id") String scenario_id,
                                 @RequestBody Task task) {
        ResultBody<Task> body = new ResultBody<Task>();
        Task result = new Task();
        try {
            if (scenario_id != null && !"".equals(scenario_id)) {
                ScenarioVersion scenarioVersion = scenarioVersionService.findByScenarioId(scenario_id);
                task.setVersionId(scenarioVersion.getVersionId());
            }
            if (task != null && !"".equals(task)) {
                result = taskService.createTask(task);
            }
            body.setResultCode(0);
            body.setResultMessage("作业创建成功");
            body.setResult(result);

        } catch (Exception e) {
            e.printStackTrace();
            body.setResultCode(1);
            body.setResultMessage(e.getMessage());
            logger.error("作业创建失败controller" + e);
        }
        return body;
    }

    /**
     * 场景作业查询
     */
    @RequestMapping(value = "/scenario/{scenario_id}/task", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public ResultBody queryTask(
            @PathVariable(value = "scenario_id") String scenario_id,
            @RequestParam(value = "task_id", required = false) String task_id) {
        ResultBody<List<Task>> body = new ResultBody<List<Task>>();
        List<Task> result = new ArrayList<Task>();
        try {
            if (null != scenario_id && !"".equals(scenario_id)) {
                //根据scenarioID查versionID
                ScenarioVersion scenarioVersion = scenarioVersionService.findByScenarioId(scenario_id);
                result = taskService.queryTask(scenario_id, scenarioVersion.getVersionId(), task_id);
                body.setResult(result);
            }
            body.setResultCode(0);
            body.setResultMessage("场景作业查询成功");
        } catch (Exception e) {
            e.printStackTrace();
            body.setResultCode(1);
            body.setResultMessage(e.getMessage());
            logger.error("场景作业查询失败controller" + e);
        }
        return body;
    }

    //作业保存
    @RequestMapping(value = "/scenario/{scenario_id}/task/{task_id}", method = RequestMethod.PUT, produces = "application/json")
    @ResponseBody
    public ResultBody saveTask(@PathVariable("scenario_id") String scenario_id,
                               @PathVariable("task_id") String task_id,
                               @RequestBody Task task) {
        ResultBody<Task> body = new ResultBody<Task>();
        Task result = new Task();
        try {
            if (null != scenario_id && !"".equals(scenario_id)) {
                //根据scenarioID查versionID
                ScenarioVersion scenarioVersion = scenarioVersionService.findByScenarioId(scenario_id);
                task.setVersionId(scenarioVersion.getVersionId());
                if (null != task_id && !"".equals(task_id)) {
                    task.setTaskId(task_id);
                }
                result = taskService.saveTask(task);
                body.setResult(result);
                body.setResultCode(0);
                body.setResultMessage("场景作业保存成功");
            }
        } catch (Exception e) {
            e.printStackTrace();
            body.setResultCode(1);
            body.setResultMessage(e.getMessage());
            logger.error("场景作业保存失败controller" + e);
        }
        return body;
    }

    //作业删除
    @RequestMapping(value = "/scenario/{scenario_id}/task/{task_id}", method = RequestMethod.DELETE, produces = "application/json")
    @ResponseBody
    public ResultBody deleteTask(@PathVariable("scenario_id") String scenario_id,
                                 @PathVariable("task_id") String task_id) {
        ResultBody<Task> body = new ResultBody<Task>();
        try {
            taskService.deleteTask(scenario_id, task_id);
            body.setResultCode(0);
            body.setResultMessage("场景作业删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            body.setResultCode(1);
            body.setResultMessage(e.getMessage());
            logger.error("场景作业删除失败controller" + e);
        }
        return body;
    }
}
