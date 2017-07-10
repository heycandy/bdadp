package com.chinasofti.ark.bdadp.controller.scenario;

import com.chinasofti.ark.bdadp.controller.bean.ResultBody;
import com.chinasofti.ark.bdadp.controller.scenario.bean.ScenarioAction;
import com.chinasofti.ark.bdadp.dao.scenario.ScenarioDao;
import com.chinasofti.ark.bdadp.entity.scenario.Scenario;
import com.chinasofti.ark.bdadp.service.queue.QueueTaskService;
import com.chinasofti.ark.bdadp.service.scenario.ScenarioService;
import com.chinasofti.ark.bdadp.service.scenario.bean.ScenarioExportTask;
import com.chinasofti.ark.bdadp.service.scenario.bean.ScenarioImportTask;
import com.chinasofti.ark.bdadp.service.scenario.bean.ScenarioServiceException;
import com.chinasofti.ark.bdadp.service.scenario.bean.ScenarioStatus;
import com.chinasofti.ark.bdadp.util.common.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

/**
 * 场景controller
 *
 * @author wgzhang
 */
@Controller
@RequestMapping(value = "/service/v1")
public class ScenarioController {

  @Autowired
  private ScenarioDao scenarioDao;

  @Autowired
  private ScenarioService scenarioService;

  @Autowired
  private QueueTaskService queueTaskService;

  /**
   * 记录日志
   */
  private Logger logger = LoggerFactory.getLogger(this.getClass());

  /**
   * 新增场景
   */
  @RequestMapping(value = "/scenario", method = RequestMethod.POST, produces = "application/json; utf-8")
  @ResponseBody
  public ResultBody addScenario(@RequestBody Scenario scenario) {
    ResultBody<Scenario> body = new ResultBody<Scenario>();
    Scenario result = new Scenario();
    try {
      if (scenario != null && !"".equals(scenario)) {
        result = scenarioService.addScenario(scenario);
        body.setResultCode(0);
        body.setResultMessage("新增成功");
        body.setResult(result);
      }
    } catch (Exception e) {
      e.printStackTrace();
      body.setResultCode(1);
      body.setResultMessage("场景新增失败" + e.getMessage());
      logger.error("场景新增失败" + e);
    }
    return body;
  }

  /**
   * 场景查询
   */
  @RequestMapping(value = "/scenario", method = RequestMethod.GET, produces = "application/json")
  @ResponseBody
  public ResultBody findScenario(
      @RequestParam(value = "scenario_id", required = false) String scenario_id) {
    ResultBody body = new ResultBody();
    List<Scenario> result = new ArrayList<Scenario>();
    try {
      if (null != scenario_id && !"".equals(scenario_id)) {
        Scenario scenario1 = scenarioService.findScenarioById(scenario_id);
        // result.set(0,scenario1);
        body.setResult(Arrays.asList(scenario1));
      } else {
        result = scenarioService.findAllScenario();
        body.setResult(result);
      }
      body.setResultCode(0);
      body.setResultMessage("查询成功");
    } catch (Exception e) {
      e.printStackTrace();
      body.setResultCode(1);
      body.setResultMessage("场景查询失败" + e.getMessage());
      logger.error("场景查询失败" + e);
    }
    return body;
  }

  /**
   * 更新场景
   */
  @RequestMapping(value = "/scenario/{scenario_id}", method = RequestMethod.PUT, produces = "application/json")
  @ResponseBody
  public ResultBody updateScenario(@PathVariable("scenario_id") String scenario_id,
                                   @RequestBody Scenario scenario) {
    ResultBody<Scenario> body = new ResultBody<Scenario>();
    Scenario result = new Scenario();
    try {
      if (StringUtils.isEmpty(scenario.getScenarioId())) {
        scenario.setScenarioId(scenario_id);
      }
      result = scenarioService.updateScenario(scenario);
      body.setResultCode(0);
      body.setResultMessage("更新成功");
      body.setResult(result);
    } catch (Exception e) {
      e.printStackTrace();
      body.setResultCode(1);
      body.setResultMessage("场景更新失败" + e.getMessage());
      logger.error("场景更新失败" + e);
    }
    return body;
  }

  /**
   * 场景删除
   */
  @RequestMapping(value = "/scenario/{scenario_id}", method = RequestMethod.DELETE, produces = "application/json")
  @ResponseBody
  public ResultBody delScenario(@PathVariable("scenario_id") String scenario_id) {
    ResultBody body = new ResultBody();
    try {
      Scenario scenario = scenarioDao.findOne(scenario_id);
      if (scenario.getScenarioStatus() == ScenarioStatus.ONLINE.ordinal()) {
        body.setResultCode(1);
        body.setResultMessage("场景: {}已经上线，无法删除！！！" + scenario.getScenarioName());
      } else {
        scenarioService.delScenario(scenario_id);
        body.setResultCode(0);
        body.setResultMessage("删除成功");
      }

    } catch (Exception e) {
      e.printStackTrace();
      body.setResultCode(1);
      body.setResultMessage("场景删除失败" + e.getMessage());
      logger.error("场景删除失败" + e);
    }
    return body;
  }

  /**
   * delete场景批量删除 cate_add批量分类场景添加 cate_remove批量分类场景移除 export 场景导出 import 场景导入
   */
  @RequestMapping(value = "/scenario/action", method = RequestMethod.POST, produces = "application/json")
  @ResponseBody
  public ResultBody delScenario(@RequestBody ScenarioAction scenarioAction,
                                HttpServletRequest request) {
    ResultBody<Object> body = new ResultBody<>();
    try {
      switch (scenarioAction.getAction()) {
        case "delete":
          int reulstCode = scenarioService.delBatchScenario(scenarioAction.getScenario_id());
          if (reulstCode == 0) {
            body.setResultMessage("删除成功");
          } else {
            body.setResultMessage("有场景已经上线，不能删除");
          }
          body.setResultCode(reulstCode);

          break;

        case "cate_add":
          scenarioService
              .addBatchCategor(scenarioAction.getCate_id(), scenarioAction.getScenario_id());
          body.setResultMessage("场景批添加成功");
          body.setResultCode(0);

          break;

        case "cate_remove":
          scenarioService
              .removeBatchCategor(scenarioAction.getCate_id(), scenarioAction.getScenario_id());
          body.setResultMessage("场景批量删除成功");
          body.setResultCode(0);

          break;

        case "export":
          String id0 = UUID.getId();
          String name0 = scenarioAction.getName();
          Collection<String> collection0 = scenarioAction.getScenario_id();
          String resType0 = scenarioAction.getResType();
          String createUser0 = scenarioAction.getUserId();

          String s0 = String.format("resources/scenario/%s/%s", id0, resType0);
//          String realPath0 = request.getSession().getServletContext().getRealPath(s0);
          String realPath0 = Paths.get(
              System.getProperty("java.io.tmpdir", "/tmp"), "ark", s0).toString();
          logger.debug(String.format("resource real path %s", realPath0));
          Files.createDirectories(Paths.get(realPath0));

          Path path0 = Paths.get(realPath0, name0 + "." + resType0);

          queueTaskService.submit(
              new ScenarioExportTask(id0, name0, collection0, path0, createUser0));

          body.setResult(id0);

          break;

        case "preview":
          String id1 = scenarioAction.getCate_id();
          String name1 = scenarioAction.getName();
          Collection<String> collection1 = scenarioAction.getScenario_id();
          String resType1 = scenarioAction.getResType();
          String createUser1 = scenarioAction.getUserId();

          String s1 = String.format("resources/scenario/%s/%s", id1, resType1);
//          String realPath1 = request.getSession().getServletContext().getRealPath(s1);
          String realPath1 = Paths.get(
              System.getProperty("java.io.tmpdir", "/tmp"), "ark", s1).toString();
          logger.debug(String.format("resource real path %s", realPath1));

          Path path1 = Paths.get(realPath1, name1 + "." + resType1);

          body.setResult(queueTaskService.preview(
              new ScenarioImportTask(id1, name1, collection1, path1, createUser1)));

          break;

        case "import":
          String id2 = scenarioAction.getCate_id();
          String name2 = scenarioAction.getName();
          Collection<String> collection2 = scenarioAction.getScenario_id();
          String resType2 = scenarioAction.getResType();
          String createUser2 = scenarioAction.getUserId();

          String s2 = String.format("resources/scenario/%s/%s", id2, resType2);
//          String realPath2 = request.getSession().getServletContext().getRealPath(s2);
          String realPath2 = Paths.get(
              System.getProperty("java.io.tmpdir", "/tmp"), "ark", s2).toString();
          logger.debug(String.format("resource real path %s", realPath2));
          Files.createDirectories(Paths.get(realPath2));

          Path path2 = Paths.get(realPath2, name2 + "." + resType2);

          queueTaskService.submit(
              new ScenarioImportTask(id2, name2, collection2, path2, createUser2));

          body.setResult(id2);

          break;

        default:

      }
    } catch (ScenarioServiceException e) {
      body.setResultArgs(e.getResultArgs());
      body.setResultCode(e.getResultCode());
      body.setResultMessage(e.getResultMessage());

      logger.error(e.getResultMessage(), e);
    } catch (Exception e) {
      body.setResultCode(1);
      body.setResultMessage(e.getMessage());
      logger.error("/scenario/action", e);
    }
    return body;
  }

}
