package com.chinasofti.ark.bdadp.service.task;

import com.chinasofti.ark.bdadp.entity.task.Task;

import java.util.List;

/**
 * Created by TongTong on 2016/8/29.
 */
public interface TaskService {

    //作业创建
    public Task createTask(Task task) throws Exception;

    //作业查询
    public List<Task> queryTask(String scenario_id, String version_id, String task_id)
            throws Exception;

    Task findTaskById(String id);

    //作业保存
    public Task saveTask(Task task) throws Exception;

    //作业删除
    public void deleteTask(String scenario_id, String task_id) throws Exception;
}

