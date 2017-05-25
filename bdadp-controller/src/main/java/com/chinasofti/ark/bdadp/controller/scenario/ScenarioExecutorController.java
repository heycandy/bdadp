package com.chinasofti.ark.bdadp.controller.scenario;

import com.chinasofti.ark.bdadp.controller.bean.ResultBody;
import com.chinasofti.ark.bdadp.service.scenario.ScenarioExecutorService;
import com.chinasofti.ark.bdadp.service.scenario.bean.ScenarioInspectCondition;
import com.chinasofti.ark.bdadp.service.scenario.bean.ScenarioServiceException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by White on 2016/09/18.
 */
@Controller
@RequestMapping(value = "/service/v1")
public class ScenarioExecutorController {

  @Autowired
  private ScenarioExecutorService service;

  private Logger logger = LoggerFactory.getLogger(this.getClass());

  @RequestMapping(value = "/scenario/{scenarioId}/execute", method = RequestMethod.POST, produces = "application/json")
  @ResponseBody
  public ResultBody execute(@PathVariable String scenarioId,
                            @RequestBody ScenarioInspectCondition condition) {
    ResultBody<String> body = new ResultBody<>();
    try {
      body.setResult(service.inspect(scenarioId, condition));
    } catch (ScenarioServiceException e) {
      body.setResultCode(e.getResultCode());
      body.setResultMessage(e.getResultMessage());
      body.setResultArgs(e.getResultArgs());

      logger.error(e.getResultMessage(), e);
    } catch (Exception e) {
      body.setResultCode(22999);
      body.setResultMessage("SSE-22999");

      logger.error("SSE-22999", e);
    }
    return body;
  }

  @RequestMapping(value = "/scenario/{scenarioId}/execute", method = RequestMethod.GET, produces = "application/json")
  @ResponseBody
  public ResultBody execute(@PathVariable String scenarioId, @RequestParam String taskId,
                            @RequestParam String executionId) {
    ResultBody<Object> body = new ResultBody<>();
    try {
      body.setResult(service.execute(taskId, executionId));
    } catch (ScenarioServiceException e) {
      body.setResultCode(e.getResultCode());
      body.setResultMessage(e.getResultMessage());
      body.setResultArgs(e.getResultArgs());

      logger.error(e.getResultMessage(), e);
    } catch (Exception e) {
      body.setResultCode(22999);
      body.setResultMessage("SSE-22999");

      logger.error("SSE-22999", e);
    }

    return body;
  }
}
