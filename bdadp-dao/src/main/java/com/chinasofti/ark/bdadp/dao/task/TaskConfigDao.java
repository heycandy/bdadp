package com.chinasofti.ark.bdadp.dao.task;

import com.chinasofti.ark.bdadp.entity.task.TaskConfig;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by Administrator on 2016/8/31.
 */
public interface TaskConfigDao extends CrudRepository<TaskConfig, String> {

    public void deleteByTaskId(String taskId);
}
