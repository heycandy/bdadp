package com.chinasofti.ark.bdadp.service.visual;

import com.chinasofti.ark.bdadp.service.visual.bean.ScenarioColumnVisual;
import com.chinasofti.ark.bdadp.service.visual.bean.ScenarioCycleVisual;
import com.chinasofti.ark.bdadp.service.visual.bean.ScenarioRadarVisual;

import java.util.List;

/**
 * Created by White on 2016/10/20.
 */
public interface ScenarioVisualService {

    ScenarioCycleVisual calcCycleVisual(String startTime, String endTime);

    List<ScenarioRadarVisual> calcRadarVisual(String startTime, String endTime) throws Exception;

    Integer calcTotalCountVisual(String startTime, String endTime);

    Integer calcTotalExecuteCountVisual(String startTime, String endTime);

    List calcLinearVisual(String startTime, String endTime) throws Exception;

    List<ScenarioColumnVisual> calcColumnVisual(String startTime, String endTime) throws Exception;
}
