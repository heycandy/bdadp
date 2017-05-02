package com.chinasofti.ark.bdadp.controller.visual;

import com.chinasofti.ark.bdadp.controller.bean.ResultBody;
import com.chinasofti.ark.bdadp.service.visual.ScenarioVisualService;
import com.chinasofti.ark.bdadp.service.visual.bean.ScenarioCycleVisual;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created by White on 2016/10/20.
 */
@Controller
@RequestMapping(value = "/service/v1")
public class ScenarioVisualController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ScenarioVisualService service;

    @RequestMapping(value = "/scenario/visual/cycle", method = RequestMethod.GET, produces = "application/json; utf-8")
    @ResponseBody
    public ResultBody getCycleVisual(
            @RequestParam(value = "startTime", required = false) String startTime,
            @RequestParam(value = "endTime", required = false) String endTime) {
        ResultBody<ScenarioCycleVisual> body = new ResultBody<>();
        try {
            body.setResult(service.calcCycleVisual(startTime, endTime));
        } catch (Exception e) {
            body.setResultCode(1);
            body.setResultMessage(e.getMessage());
            logger.error(String.format("/scenario/visual/cycle?%s&%s", startTime, endTime), e);
        }
        return body;
    }

    @RequestMapping(value = "/scenario/visual/radar", method = RequestMethod.GET, produces = "application/json; utf-8")
    @ResponseBody
    public ResultBody getRadarVisual(
            @RequestParam(value = "startTime", required = false) String startTime,
            @RequestParam(value = "endTime", required = false) String endTime) {
        ResultBody<List> body = new ResultBody<>();
        try {
            body.setResult(service.calcRadarVisual(startTime, endTime));
        } catch (Exception e) {
            body.setResultCode(1);
            body.setResultMessage(e.getMessage());
            logger.error(String.format("/scenario/visual/radar?%s&%s", startTime, endTime), e);
        }
        return body;
    }

    @RequestMapping(value = "/scenario/visual/totalcount", method = RequestMethod.GET, produces = "application/json; utf-8")
    @ResponseBody
    public ResultBody getTotalCountVisual(
            @RequestParam(value = "startTime", required = false) String startTime,
            @RequestParam(value = "endTime", required = false) String endTime) {
        ResultBody<Integer> body = new ResultBody<>();
        try {
            body.setResult(service.calcTotalCountVisual(startTime, endTime));
        } catch (Exception e) {
            body.setResultCode(1);
            body.setResultMessage(e.getMessage());
            logger.error(String.format("/scenario/visual/totalcount?%s&%s", startTime, endTime), e);
        }
        return body;
    }

    @RequestMapping(value = "/scenario/visual/totalexecnt", method = RequestMethod.GET, produces = "application/json; utf-8")
    @ResponseBody
    public ResultBody getTotalUsageVisual(
            @RequestParam(value = "startTime", required = false) String startTime,
            @RequestParam(value = "endTime", required = false) String endTime) {
        ResultBody<Integer> body = new ResultBody<>();
        try {
            body.setResult(service.calcTotalExecuteCountVisual(startTime, endTime));
        } catch (Exception e) {
            body.setResultCode(1);
            body.setResultMessage(e.getMessage());
            logger.error(String.format("/scenario/visual/totalusage?%s&%s", startTime, endTime), e);
        }
        return body;
    }

    @RequestMapping(value = "/scenario/visual/linear", method = RequestMethod.GET, produces = "application/json; utf-8")
    @ResponseBody
    public ResultBody getLinearVisual(
            @RequestParam(value = "startTime", required = false) String startTime,
            @RequestParam(value = "endTime", required = false) String endTime) {
        ResultBody<List> body = new ResultBody<>();
        try {
            body.setResult(service.calcLinearVisual(startTime, endTime));
        } catch (Exception e) {
            body.setResultCode(1);
            body.setResultMessage(e.getMessage());
            logger.error(String.format("/scenario/visual/linear?%s&%s", startTime, endTime), e);
        }
        return body;
    }

    @RequestMapping(value = "/scenario/visual/column", method = RequestMethod.GET, produces = "application/json; utf-8")
    @ResponseBody
    public ResultBody getColumnVisual(
            @RequestParam(value = "startTime", required = false) String startTime,
            @RequestParam(value = "endTime", required = false) String endTime) {
        ResultBody<List> body = new ResultBody<>();
        try {
            body.setResult(service.calcColumnVisual(startTime, endTime));
        } catch (Exception e) {
            body.setResultCode(1);
            body.setResultMessage(e.getMessage());
            logger.error(String.format("/scenario/visual/column?%s&%s", startTime, endTime), e);
        }
        return body;
    }
}
