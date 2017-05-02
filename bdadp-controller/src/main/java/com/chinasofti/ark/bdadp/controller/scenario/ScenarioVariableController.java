package com.chinasofti.ark.bdadp.controller.scenario;

import com.chinasofti.ark.bdadp.controller.bean.ResultBody;
import com.chinasofti.ark.bdadp.controller.schedule.DTO.ScenarioUserVariableDTO;
import com.chinasofti.ark.bdadp.entity.scenario.ScenarioUserVariable;
import com.chinasofti.ark.bdadp.service.scenario.ScenarioVariableService;
import com.chinasofti.ark.bdadp.util.common.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

/**
 * Created by White on 2016/09/12.
 */
@Controller
@RequestMapping(value = "/service/v1")
public class ScenarioVariableController {

    @Autowired
    private ScenarioVariableService service;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 增加用户自定义变量
     */
    @SuppressWarnings("rawtypes")
    @RequestMapping(value = "/scenario/variable", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResultBody create(@RequestBody ScenarioUserVariableDTO dto) {
        ResultBody<ScenarioUserVariable> body = new ResultBody<>();
        try {
            ScenarioUserVariable bean = new ScenarioUserVariable();
            BeanUtils.copyProperties(dto, bean);
            bean.setVariableId(UUID.randomUUID().toString());
            body.setResult(service.create(bean));
        } catch (Exception e) {
            logger.error("/scenario/variable", e);
            body.setResultCode(1);
            body.setResultMessage(e.getMessage());
        }

        return body;
    }

    /**
     * 根据场景id获取所有定义的变量
     */
    @SuppressWarnings("rawtypes")
    @RequestMapping(value = "/scenario/{scenario_id}/variables", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResultBody get(@PathVariable String scenario_id) {
        ResultBody<Iterable> body = new ResultBody<>();
        try {
            body.setResult(service.findByScenarioId(scenario_id));
        } catch (Exception e) {
            logger.error("/scenario/{scenario_id}/variables", e);
            body.setResultCode(1);
            body.setResultMessage(e.getMessage());
        }

        return body;
    }

    @SuppressWarnings("rawtypes")
    @RequestMapping(value = "/scenario/variables", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResultBody parseVariable(@RequestParam(value = "inputVariable") String inputVariable,
                                    @RequestParam(value = "scenarioId") String scenarioId) {
        ResultBody<String> body = new ResultBody<>();
        try {
            body.setResult(service.parseVariable(inputVariable, scenarioId));
//			body.setResult("==============");
        } catch (Exception e) {
            logger.error("/scenario/{scenario_id}/variables", e);
            body.setResultCode(1);
            body.setResultMessage(e.getMessage());
        }

        return body;
    }

    /**
     * 更新一个用户变量
     */
    @SuppressWarnings("rawtypes")
    @RequestMapping(value = "/scenario/variable/{variableId}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResultBody update(@PathVariable String variableId, @RequestBody ScenarioUserVariable s) {
        ResultBody<ScenarioUserVariable> body = new ResultBody<>();
        try {
            if (StringUtils.isEmpty(s.getVariableId())) {
                s.setVariableId(variableId);
            }
            body.setResult(service.update(s));
        } catch (Exception e) {
            logger.error("/scenario/variable/{variableId}", e);
            body.setResultCode(1);
            body.setResultMessage(e.getMessage());
        }

        return body;
    }

    /**
     * 删除用户变量
     */
    @SuppressWarnings("rawtypes")
    @RequestMapping(value = "/scenario/variable/{variableId}", method = RequestMethod.DELETE, produces = "application/json")
    @ResponseBody
    public ResultBody delete(@PathVariable String variableId) {
        ResultBody<ScenarioUserVariable> body = new ResultBody<>();
        try {
            service.delete(variableId);
        } catch (Exception e) {
            logger.error("/scenario/variable/{variableId}", e);
            body.setResultCode(1);
            body.setResultMessage(e.getMessage());
        }

        return body;
    }
}
