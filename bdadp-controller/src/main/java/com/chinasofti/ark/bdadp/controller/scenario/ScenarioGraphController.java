package com.chinasofti.ark.bdadp.controller.scenario;

import com.chinasofti.ark.bdadp.controller.bean.ResultBody;
import com.chinasofti.ark.bdadp.entity.scenario.ScenarioGraphDAG;
import com.chinasofti.ark.bdadp.service.scenario.ScenarioGraphDagService;
import com.chinasofti.ark.bdadp.service.scenario.bean.ScenarioServiceException;
import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by White on 2016/9/1.
 */
@Controller
@RequestMapping(value = "/service/v1")
public class ScenarioGraphController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ScenarioGraphDagService scenarioGraphDagService;

    @ResponseBody
    @RequestMapping(value = "/scenario/graph", method = RequestMethod.POST, produces = "application/json")
    public ResultBody saveScenarioGraph(@RequestBody ScenarioGraphDAG scenarioGraphDAG) {
        ResultBody<ScenarioGraphDAG> body = new ResultBody<>();
        try {
            Assert.notNull(scenarioGraphDAG.getScenarioId());

            ScenarioGraphDAG result =
                    scenarioGraphDagService.createScenarioGraph(scenarioGraphDAG);

            body.setResultCode(0);
            body.setResult(result);
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

    /**
     * 场景查询
     */
    @RequestMapping(value = "/scenario/graph", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public ResultBody findScenarioGraph(
            @RequestParam(name = "scenario_id", required = true) String scenario_id,
            @RequestParam(name = "version_id", required = false) String version_id,
            HttpServletRequest request) {
        ResultBody<ScenarioGraphDAG> body = new ResultBody<>();
        String language = request.getHeader("Accept-Language");
        try {
            if (Strings.isNullOrEmpty(version_id)) {
                body.setResult(scenarioGraphDagService.findScenarioByScenarioId(scenario_id, language));
            } else {
                body.setResult(scenarioGraphDagService
                        .findScenarioByScenarioIdAndByVersionId(scenario_id, version_id));
            }

            body.setResultCode(0);
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

    @ResponseBody
    @RequestMapping(value = "/scenario/graph/{graph_id}", method = RequestMethod.PUT, produces = "application/json")
    public ResultBody updateScenarioGraph(
            @PathVariable("graph_id") String graph_id,
            @RequestBody ScenarioGraphDAG scenarioGraphDAG) {
        ResultBody<ScenarioGraphDAG> body = new ResultBody<>();
        try {
            if (StringUtils.isEmpty(scenarioGraphDAG.getGraphId())) {
                scenarioGraphDAG.setGraphId(graph_id);
            }

            ScenarioGraphDAG result =
                    scenarioGraphDagService.updateScenarioGraph(scenarioGraphDAG);

            body.setResultCode(0);
            body.setResult(result);
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
