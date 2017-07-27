package com.chinasofti.ark.bdadp.controller.user;

import com.chinasofti.ark.bdadp.controller.bean.ResultBody;
import com.chinasofti.ark.bdadp.entity.user.User;
import com.chinasofti.ark.bdadp.service.user.UserSecurityService;
import com.chinasofti.ark.bdadp.service.user.UserService;
import javax.servlet.http.HttpServletRequest;
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
public class UserController {

  private Logger logger = LoggerFactory.getLogger(this.getClass());

  @Autowired
  private UserService service;

  @Autowired
  private UserSecurityService secService;

  @ResponseBody
  @RequestMapping(value = "/user", method = RequestMethod.POST, produces = "application/json")
  public ResultBody create(@RequestBody User s) {
    ResultBody<User> body = new ResultBody<>();
    try {
      body.setResult(service.create(s));
    } catch (Exception e) {
      logger.error("/user", e);
      body.setResultCode(1);
      body.setResultMessage(e.getMessage());
    }

    return body;
  }

  @ResponseBody
  @RequestMapping(value = "/user", method = RequestMethod.GET, produces = "application/json")
  public ResultBody find(@RequestParam(value = "id", required = false) String id) {
    try {
      if (StringUtils.isEmpty(id)) {
        ResultBody<Iterable> body = new ResultBody<>();
        Iterable<User> result = service.findAll();
        body.setResult(result);
        return body;
      } else {
        ResultBody<User> body = new ResultBody<>();
        User result = service.findOne(id);
        body.setResult(result);
        return body;
      }
    } catch (Exception e) {
      logger.error("/user", e);
      ResultBody body = new ResultBody();
      body.setResultCode(1);
      body.setResultMessage(e.getMessage());

      return body;
    }
  }

  @ResponseBody
  @RequestMapping(value = "/user/{id}", method = RequestMethod.PUT, produces = "application/json")
  public ResultBody update(@PathVariable String id, @RequestBody User s) {
    ResultBody<User> body = new ResultBody<>();
    try {
      if (StringUtils.isEmpty(id)) {
        s.setUserId(id);
      }
      body.setResult(service.update(s));
    } catch (Exception e) {
      logger.error("/user", e);
      body.setResultCode(1);
      body.setResultMessage(e.getMessage());
    }

    return body;
  }

  @ResponseBody
  @RequestMapping(value = "/user/{id}", method = RequestMethod.DELETE, produces = "application/json")
  public ResultBody delete(@PathVariable String id) {
    ResultBody body = new ResultBody();
    try {
      service.delete(id);
    } catch (Exception e) {
      logger.error("/user", e);
      body.setResultCode(1);
      body.setResultMessage(e.getMessage());
    }

    return body;
  }

  @ResponseBody
  @RequestMapping(value = "/user/login", method = RequestMethod.POST, produces = "application/json")
  public ResultBody login(HttpServletRequest request, @RequestBody User s) {
    ResultBody<String> body = new ResultBody<>();
    try {
      String token = secService.login(s);
      if (StringUtils.isEmpty(token)) {
        body.setResultCode(1);
        body.setResultMessage("login failed.");
      } else {
        String loginUrl =
            "http://" + request.getServerName() + ":" + request.getServerPort() + request
                .getContextPath();
        System.setProperty("loginUrl", loginUrl
        );
        body.setResult(token);
      }

    } catch (Exception e) {
      logger.error("/user/login", e);
      body.setResultCode(1);
      body.setResultMessage(e.getMessage());
    }

    return body;
  }

  @ResponseBody
  @RequestMapping(value = "/user/logout", method = RequestMethod.POST, produces = "application/json")
  public ResultBody logout(@RequestBody User s) {
    ResultBody<String> body = new ResultBody<>();
    try {
      secService.logout(s);
    } catch (Exception e) {
      logger.error("/user/logout", e);
      body.setResultCode(1);
      body.setResultMessage(e.getMessage());
    }

    return body;
  }
}
