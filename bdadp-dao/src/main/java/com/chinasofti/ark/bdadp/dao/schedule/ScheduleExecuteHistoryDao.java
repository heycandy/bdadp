package com.chinasofti.ark.bdadp.dao.schedule;

import com.chinasofti.ark.bdadp.entity.schedule.ScenarioExecuteHistory;
import com.chinasofti.ark.bdadp.entity.schedule.ScenarioExecuteHistoryPK;
import com.chinasofti.ark.bdadp.util.dto.MonitorDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
public interface ScheduleExecuteHistoryDao extends JpaRepository<ScenarioExecuteHistory, String> {

    ScenarioExecuteHistory findByUnionId(ScenarioExecuteHistoryPK unionId);

  @Query("select new com.chinasofti.ark.bdadp.util.dto.MonitorDTO(s.taskName ,s.unionId.executionId, s.unionId.taskId, s.scenarioId,s.executeStatus ,s.startTime,s.endTime,s.executeProgress ,s.createTime) from com.chinasofti.ark.bdadp.entity.schedule.ScenarioExecuteHistory s where  s.executeStatus = :executeStatus and s.startTime >= :searchDate and s.scenarioId = s.unionId.taskId order by s.startTime desc")
    Page<MonitorDTO> getPageJobByStatusAndSearchday(Pageable page,
                                                    @Param(value = "executeStatus") int executeStatus,
                                                    @Param(value = "searchDate") Date searchDate);

  @Query("select new com.chinasofti.ark.bdadp.util.dto.MonitorDTO(s.taskName ,s.unionId.executionId, s.unionId.taskId, s.scenarioId,s.executeStatus ,s.startTime,s.endTime,s.executeProgress ,s.createTime) from com.chinasofti.ark.bdadp.entity.schedule.ScenarioExecuteHistory s where  s.startTime >= :searchDate and s.scenarioId = s.unionId.taskId order by s.startTime desc")
    Page<MonitorDTO> getPageJobsBySearchday(Pageable page,
                                            @Param(value = "searchDate") Date searchDate);

  @Query("select new com.chinasofti.ark.bdadp.util.dto.MonitorDTO(s.taskName ,s.unionId.executionId, s.unionId.taskId, s.scenarioId,s.executeStatus ,s.startTime,s.endTime,s.executeProgress ,s.createTime) from com.chinasofti.ark.bdadp.entity.schedule.ScenarioExecuteHistory s where  s.executeStatus = :executeStatus and s.startTime >= :searchDate and s.scenarioId in (:scenarioIds) and s.scenarioId = s.unionId.taskId order by s.startTime desc")
    Page<MonitorDTO> getPageJobsByStatusAndSearchdayAndScenarioIds(Pageable page,
                                                                   @Param(value = "executeStatus") int executeStatus,
                                                                   @Param(value = "searchDate") Date searchDate,
                                                                   @Param(value = "scenarioIds") List<String> scenarioIds);

  @Query("select new com.chinasofti.ark.bdadp.util.dto.MonitorDTO(s.taskName ,s.unionId.executionId, s.unionId.taskId, s.scenarioId,s.executeStatus ,s.startTime,s.endTime,s.executeProgress ,s.createTime) from com.chinasofti.ark.bdadp.entity.schedule.ScenarioExecuteHistory s where  s.unionId.executionId = :executionId and   s.scenarioId = :scenarioId and s.scenarioId != s.unionId.taskId order by s.startTime asc")
    Page<MonitorDTO> getPageTasks(Pageable page, @Param(value = "executionId") String executionId,
                                  @Param(value = "scenarioId") String scenarioId);

  @Query("select new com.chinasofti.ark.bdadp.util.dto.MonitorDTO(s.taskName ,s.unionId.executionId, s.unionId.taskId, s.scenarioId,s.executeStatus ,s.startTime,s.endTime,s.executeProgress ,s.createTime) from com.chinasofti.ark.bdadp.entity.schedule.ScenarioExecuteHistory s where  s.startTime >= :searchDate and s.scenarioId = s.unionId.taskId and s.scenarioId in (:scenarioIds)  order by s.startTime desc")
    Page<MonitorDTO> getPageJobsBySearchdayAndScenarioIds(Pageable page,
                                                          @Param(value = "searchDate") Date searchDate,
                                                          @Param(value = "scenarioIds") List<String> scenarioIds);
}
