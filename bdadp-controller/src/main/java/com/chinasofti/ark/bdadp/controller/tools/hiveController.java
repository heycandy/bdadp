package com.chinasofti.ark.bdadp.controller.tools;

import com.chinasofti.ark.bdadp.controller.bean.ResultBody;
import com.chinasofti.ark.bdadp.service.tools.HiveInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Created by msc on 2016/9/22.
 */
@Controller
@RequestMapping(value = "/service/v1")
public class hiveController {

    @Autowired
    private HiveInfoService service;

    /**
     * @return
     */
    @RequestMapping(value = "/get/tablesAndColumns", method = RequestMethod.GET)
    @ResponseBody
    public ResultBody getTablesAndColumns(@RequestParam(value = "dbName") String dbName) {
        ResultBody body = new ResultBody();
        try {
            Map<String, List<String>> resultList = service.getTablesAndColumns(dbName);
            body.setResult(resultList);
            body.setResultCode(0);
            body.setResultMessage("操作成功!");
            return body;
        } catch (Exception e) {
            e.printStackTrace();
            body.setResultCode(1);
            body.setResultMessage("操作失败!" + e.getMessage());
            return body;
        }
    }

    /**
     * 根据集群ID、服务组件ID，查询服务组件的所有数据库列表
     *
     * @return responseEntity
     */
    @RequestMapping(value = "/get/dbNameList", method = RequestMethod.GET)
    @ResponseBody
    public ResultBody getDbNameList() {
        ResultBody body = new ResultBody();
        try {
            List<String> resultList = service.getDbNameList();
            body.setResult(resultList);
            body.setResultCode(0);
            body.setResultMessage("操作成功!");
            return body;
        } catch (Exception e) {

            e.printStackTrace();
            body.setResultCode(1);
            body.setResultMessage("操作失败!" + e.getMessage());
            return body;
        }
    }

    /**
     * 根据集群ID、服务组件ID、数据库名，查询所有表名列表
     *
     * @return responseEntity
     */
    @RequestMapping(value = "/get/tableNameList", method = RequestMethod.GET)
    @ResponseBody
    public ResultBody getTableNameList(@RequestParam(value = "dbName") String dbName) {
        ResultBody body = new ResultBody();
        try {
            List<String> resultList = service.getTableNameList(dbName);
            body.setResult(resultList);
            body.setResultCode(0);
            body.setResultMessage("操作成功!");
            return body;
        } catch (Exception e) {

            e.printStackTrace();
            body.setResultCode(1);
            body.setResultMessage("操作失败!" + e.getMessage());
            return body;
        }
    }

    /**
     * 根据集群ID、服务组件ID、数据库名、表名，查询表的描述信息(列名（列类型）)
     *
     * @return responseEntity
     */
    @RequestMapping(value = "/get/tableDescInfoList", method = RequestMethod.GET)
    @ResponseBody
    public ResultBody getTableNameList(@RequestParam(value = "dbName") String dbName,
                                       @RequestParam(value = "tableName") String tableName) {
        ResultBody body = new ResultBody();
        try {
            List<String> resultList = service.getTableDescInfoList(dbName, tableName);
            body.setResult(resultList);
            body.setResultCode(0);
            body.setResultMessage("操作成功!");
            return body;
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            body.setResultCode(1);
            body.setResultMessage("操作失败!" + e.getMessage());
            return body;
        }
    }


    /**
     * 执行HQL语句
     *
     * @return responseEntity
     */
    @RequestMapping(value = "/execHql", method = RequestMethod.POST)
    @ResponseBody
    public ResultBody execHql(@RequestBody Map paramMap) {
        ResultBody body = new ResultBody();
        Object oMaxRows = paramMap.get("maxRows");
        try {
            int maxRows = (oMaxRows != null && !"".equals(((String) oMaxRows).trim())
                    && Integer.valueOf((String) oMaxRows).intValue() > 0) ? Integer
                    .valueOf((String) oMaxRows).intValue() : 1000;
            List<Object> sce = service
                    .execHql1((String) paramMap.get("dbName"), (String) paramMap.get("hqlStats"), maxRows);
            body.setResult(sce);
            body.setResultCode(0);
            body.setResultMessage("操作成功!");
            return body;
        } catch (Exception e) {
            body.setResultCode(1);
            body.setResultMessage("操作失败!" + e.getMessage());
            return body;
        }
    }
}
