package com.chinasofti.ark.bdadp.dao.schedule;

import com.chinasofti.ark.bdadp.entity.schedule.ScheduleHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

/**
 * @Author : water
 * @Date : 2016年9月9日
 * @Desc : 任务调度接口
 * @version: V1.0
 */
public interface ScheduleHistoryDao extends JpaRepository<ScheduleHistory, String> {

    @Query("select s from com.chinasofti.ark.bdadp.entity.schedule.ScheduleHistory s  where s.scenarioId = :scenarioId and  s.startTime >= :sTime and s.endTime <= :eTime")
    List<ScheduleHistory> sumScenarioSchedule(@Param(value = "scenarioId") String scenarioId,
                                              @Param(value = "sTime") Date sTime,
                                              @Param(value = "eTime") Date eTime);

    @Query("select s from com.chinasofti.ark.bdadp.entity.schedule.ScheduleHistory s  group by s.scenarioId ")
    List<ScheduleHistory> findGroupBy();

    @Query("select s from com.chinasofti.ark.bdadp.entity.schedule.ScheduleHistory s  where s.scenarioId = :scenarioId and  s.startTime >= :sTime and s.endTime <= :eTime and s.scheduleStatus = :scheduleStatus")
    List<ScheduleHistory> sumScenarioScheduleSuccess(@Param(value = "scenarioId") String scenarioId,
                                                     @Param(value = "sTime") Date sTime,
                                                     @Param(value = "eTime") Date eTime,
                                                     @Param(value = "scheduleStatus") String scheduleStatus);
}
