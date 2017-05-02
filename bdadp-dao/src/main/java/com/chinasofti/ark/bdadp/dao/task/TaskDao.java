package com.chinasofti.ark.bdadp.dao.task;

import com.chinasofti.ark.bdadp.entity.task.Task;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by TongTong on 2016/8/29.
 */
public interface TaskDao extends CrudRepository<Task, String> {

    //查询某个场景下的所有task
    public List<Task> findByVersionId(String versionId);

    //查询某个场景下的某个task
    public List<Task> findByVersionIdAndTaskId(String versionId, String taskId);
}
