package com.chinasofti.ark.bdadp.controller.scenario;

import com.chinasofti.ark.bdadp.controller.bean.ResultBody;
import com.chinasofti.ark.bdadp.entity.scenario.ScenarioCategory;
import com.chinasofti.ark.bdadp.service.scenario.ScenarioCategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * Created by White on 2016/08/27.
 */
@Controller
@RequestMapping(value = "/service/v1")
public class ScenarioCategoryController {

    /**
     * 记录日志
     */
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ScenarioCategoryService service;

    /**
     * 场景分类创建
     */
    @RequestMapping(value = "/scenario/category", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public ResultBody createCategory(@RequestBody ScenarioCategory s) {
        ResultBody<ScenarioCategory> body = new ResultBody<>();
        try {
            body.setResult(service.createCategory(s));
            body.setResultMessage("场景分类创建成功");
        } catch (Exception e) {
            logger.error("/scenario/category", e);
            body.setResultCode(1);
            body.setResultMessage("场景分类创建失败" + e.getMessage());
            logger.error("场景分类创建失败" + e);
        }
        return body;
    }

    /**
     * 场景分类查询
     */
    @RequestMapping(value = "/scenario/category", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public ResultBody getAllCategory() {
        ResultBody<Iterable> body = new ResultBody<>();
        try {
            body.setResult(service.getAllCategory());
            body.setResultMessage("场景分类查询成功");
        } catch (Exception e) {
            logger.error("/scenario/category", e);
            body.setResultCode(1);
            body.setResultMessage("场景分类查询失败" + e.getMessage());
            logger.error("场景分类查询失败" + e);
        }
        return body;
    }

    /**
     * 场景分类删除
     */
    @RequestMapping(value = "/scenario/category/{cate_id}", method = RequestMethod.DELETE, produces = "application/json")
    @ResponseBody
    public ResultBody delCategory(@PathVariable String cate_id) {
        ResultBody body = new ResultBody();
        try {
            service.delCategory(cate_id);
            body.setResultMessage("场景分类删除成功");
        } catch (Exception e) {
            logger.error("/scenario/category", e);
            body.setResultCode(1);
            body.setResultMessage("场景分类删除失败" + e.getMessage());
            logger.error("场景分类删除失败" + e);
        }
        return body;
    }


}
