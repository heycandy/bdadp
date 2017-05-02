package com.chinasofti.ark.bdadp.controller.components;

import com.chinasofti.ark.bdadp.controller.bean.ResultBody;
import com.chinasofti.ark.bdadp.service.components.ComponentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Administrator on 2016/8/27.
 */
@Controller
@RequestMapping(value = "/service/v1")
public class ComponentController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ComponentService service;

    @RequestMapping(value = "/components/base", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public ResultBody getComponentBase(HttpServletRequest request) {
        ResultBody<Iterable> body = new ResultBody<>();
        String language = request.getHeader("Accept-Language");
        try {
            body.setResult(service.getComponentsBase(language));
        } catch (Exception e) {
            logger.error("/components/base", e);
            body.setResultCode(1);
            body.setResultMessage(e.getMessage());
        }
        return body;
    }

    @RequestMapping(value = "/components/business", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public ResultBody getComponentBusiness(HttpServletRequest request) {
        ResultBody<Iterable> body = new ResultBody<>();
        String language = request.getHeader("Accept-Language");
        try {
            body.setResult(service.getComponentsBusiness(language));
        } catch (Exception e) {
            logger.error("/components/business", e);
            body.setResultCode(1);
            body.setResultMessage(e.getMessage());
        }
        return body;
    }

    @RequestMapping(value = "/components/{component_id}/configs", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public ResultBody findByComponentId(@PathVariable String component_id,
                                        HttpServletRequest request) {
        ResultBody<Iterable> body = new ResultBody<>();
        String language = request.getHeader("Accept-Language");
        try {
            body.setResult(service.findComponentConfigs(component_id, language));
        } catch (Exception e) {
            logger.error(String.format("/components/%s/configs", component_id), e);
            body.setResultCode(1);
            body.setResultMessage(e.getMessage());
        }
        return body;
    }

}
