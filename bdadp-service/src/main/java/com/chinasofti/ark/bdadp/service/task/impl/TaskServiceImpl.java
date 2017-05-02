package com.chinasofti.ark.bdadp.service.task.impl;

import com.chinasofti.ark.bdadp.dao.scenario.ScenarioVersionDao;
import com.chinasofti.ark.bdadp.dao.task.TaskConfigDao;
import com.chinasofti.ark.bdadp.dao.task.TaskDao;
import com.chinasofti.ark.bdadp.entity.scenario.ScenarioVersion;
import com.chinasofti.ark.bdadp.entity.task.Task;
import com.chinasofti.ark.bdadp.entity.task.TaskConfig;
import com.chinasofti.ark.bdadp.service.task.TaskService;
import com.chinasofti.ark.bdadp.util.common.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by TongTong on 2016/8/29.
 */
@Service
@Transactional
public class TaskServiceImpl implements TaskService {

    @Autowired
    private TaskDao taskDao;
    @Autowired
    private ScenarioVersionDao scenarioVersionDao;
    @Autowired
    private TaskConfigDao taskConfigDao;
    /**
     * 记录日志
     */
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 作业创建
     */
    public Task createTask(Task task) throws Exception {
        Task taskResult = new Task();
        try {
            task.setTaskId(UUID.randomUUID().toString());
            task.setCreateTime(new Date());
            task.setTaskStatus(0);
            taskResult = taskDao.save(task);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("作业创建失败service" + e);
        }
        return taskResult;
    }

    /**
     * 作业查询
     */
    public List<Task> queryTask(String scenario_id, String version_id, String task_id)
            throws Exception {
        List<Task> list = new ArrayList<Task>();
        try {
            if (null != scenario_id && !"".equals(scenario_id)) {
                if (null != task_id && !"".equals(task_id)) {
                    //通过scenario_id和task_id获取某个task
                    list = taskDao.findByVersionIdAndTaskId(version_id, task_id);
                } else {
                    //通过scenario_id获取所有task
                    list = taskDao.findByVersionId(version_id);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("作业查询失败service" + e);
        }
        return list;
    }

    @Override
    public Task findTaskById(String id) {
        return taskDao.findOne(id);
    }

    /**
     * 作业保存
     */
    public Task saveTask(Task task) throws Exception {
        Task task1 = new Task();
        try {
            taskConfigDao.deleteByTaskId(task.getTaskId());
            List<TaskConfig> myList = task.getTaskConfigs();
            for (int i = 0; i < myList.size(); i++) {
                myList.get(i).setConfigId(UUID.randomUUID().toString());
            }
            taskConfigDao.save(myList);
            task.setCreateTime(new Date());
            task.setTaskStatus(1);
            task1 = taskDao.save(task);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("作业保存失败service" + e);
        }
        return task1;
    }

    /**
     * 作业删除
     */
    public void deleteTask(String scenario_id, String task_id) throws Exception {
        Task task = new Task();
        //根据scenarioID查找versionID
        String version_id;
        try {
            int task_status = 2;
            task.setTaskId(task_id);
            task.setTaskStatus(task_status);
            List<ScenarioVersion> myList =
                    scenarioVersionDao.findByScenarioIdOrderByCreateTimeDesc(scenario_id);
            ScenarioVersion scenarioVersion = myList.get(0);
            version_id = scenarioVersion.getVersionId();
            task.setVersionId(version_id);
            task.setCreateTime(new Date());
            taskDao.save(task);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("作业删除失败service" + e);
        }
    }

    private enum TaskStatus {
        ON_READY,
        READY,
        DELETE
    }
}
