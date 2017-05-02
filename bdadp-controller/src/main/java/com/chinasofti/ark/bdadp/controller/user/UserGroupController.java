package com.chinasofti.ark.bdadp.controller.user;

import com.chinasofti.ark.bdadp.controller.bean.ResultBody;
import com.chinasofti.ark.bdadp.entity.user.UserGroup;
import com.chinasofti.ark.bdadp.service.user.UserGroupService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

/**
 * Created by White on 2016/08/31.
 */
@Controller
@RequestMapping(value = "/service/v1")
public class UserGroupController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UserGroupService service;

    @ResponseBody
    @RequestMapping(value = "/user/group", method = RequestMethod.POST, produces = "application/json")
    public ResultBody create(@RequestBody UserGroup s) {
        ResultBody<UserGroup> body = new ResultBody<>();
        try {
            body.setResult(service.create(s));
        } catch (Exception e) {
            logger.error("/user/group", e);
            body.setResultCode(1);
            body.setResultMessage(e.getMessage());
        }

        return body;
    }

    @ResponseBody
    @RequestMapping(value = "/user/group", method = RequestMethod.GET, produces = "application/json")
    public ResultBody find(@RequestParam(value = "groupId", required = false) String groupId) {
        try {
            if (StringUtils.isEmpty(groupId)) {
                ResultBody<Iterable> body = new ResultBody<>();
                body.setResult(service.findAll());
                return body;
            } else {
                ResultBody<UserGroup> body = new ResultBody<>();
                body.setResult(service.findOne(groupId));
                return body;
            }
        } catch (Exception e) {
            logger.error("/user/group", e);
            ResultBody body = new ResultBody();
            body.setResultCode(1);
            body.setResultMessage(e.getMessage());

            return body;
        }
    }

    @ResponseBody
    @RequestMapping(value = "/user/group/{id}", method = RequestMethod.PUT, produces = "application/json")
    public ResultBody update(@PathVariable String id, @RequestBody UserGroup s) {
        ResultBody<UserGroup> body = new ResultBody<>();
        try {
            if (StringUtils.isEmpty(id)) {
                s.setGroupId(id);
            }
            body.setResult(service.update(s));
        } catch (Exception e) {
            logger.error("/user/group", e);
            body.setResultCode(1);
            body.setResultMessage(e.getMessage());
        }

        return body;
    }

    @ResponseBody
    @RequestMapping(value = "/user/group/{id}", method = RequestMethod.DELETE, produces = "application/json")
    public ResultBody delete(@PathVariable String id) {
        ResultBody body = new ResultBody();
        try {
            service.delete(id);
        } catch (Exception e) {
            logger.error("/user/group", e);
            body.setResultCode(1);
            body.setResultMessage(e.getMessage());
        }

        return body;
    }
}
