package com.chinasofti.ark.bdadp.dao.schedule;

import com.chinasofti.ark.bdadp.entity.schedule.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @Author : water
 * @Date : 2016年9月9日
 * @Desc : 任务调度接口
 * @version: V1.0
 */
public interface ScheduleDao extends JpaRepository<Schedule, String> {


}
