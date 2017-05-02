package com.chinasofti.ark.bdadp.controller.user;

import com.chinasofti.ark.bdadp.controller.bean.ResultBody;
import com.chinasofti.ark.bdadp.entity.user.Permission;
import com.chinasofti.ark.bdadp.service.user.PermissionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

/**
 * Created by White on 2016/08/30.
 */
@Controller
@RequestMapping(value = "/service/v1")
public class PermissionController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private PermissionService service;

    @ResponseBody
    @RequestMapping(value = "/user/role/permission", method = RequestMethod.POST, produces = "application/json")
    public ResultBody create(@RequestBody Permission s) {
        ResultBody<Permission> body = new ResultBody<>();
        try {
            body.setResult(service.create(s));
        } catch (Exception e) {
            logger.error("/user/role/permission", e);
            body.setResultCode(1);
            body.setResultMessage(e.getMessage());
        }

        return body;
    }

    @ResponseBody
    @RequestMapping(value = "/user/role/permission", method = RequestMethod.GET, produces = "application/json")
    public ResultBody find(@RequestParam(value = "id", required = false) String id) {
        try {
            if (StringUtils.isEmpty(id)) {
                ResultBody<Iterable> body = new ResultBody<>();
                body.setResult(service.findAll());
                return body;
            } else {
                ResultBody<Permission> body = new ResultBody<>();
                body.setResult(service.findOne(id));
                return body;
            }
        } catch (Exception e) {
            logger.error("/user/role/permission", e);
            ResultBody body = new ResultBody();
            body.setResultCode(1);
            body.setResultMessage(e.getMessage());

            return body;
        }
    }

    @ResponseBody
    @RequestMapping(value = "/user/role/permission/{id}", method = RequestMethod.PUT, produces = "application/json")
    public ResultBody update(@PathVariable String id, @RequestBody Permission s) {
        ResultBody<Permission> body = new ResultBody<>();
        try {
            if (StringUtils.isEmpty(id)) {
                s.setPermissionId(id);
            }
            body.setResult(service.update(s));
        } catch (Exception e) {
            logger.error("/user/role/permission", e);
            body.setResultCode(1);
            body.setResultMessage(e.getMessage());
        }

        return body;
    }

    @ResponseBody
    @RequestMapping(value = "/user/role/permission/{id}", method = RequestMethod.DELETE, produces = "application/json")
    public ResultBody delete(@PathVariable String id) {
        ResultBody body = new ResultBody();
        try {
            service.delete(id);
        } catch (Exception e) {
            logger.error("/user/role/permission", e);
            body.setResultCode(1);
            body.setResultMessage(e.getMessage());
        }

        return body;
    }
}
