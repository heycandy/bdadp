package com.chinasofti.ark.bdadp.controller.tools;

import com.chinasofti.ark.bdadp.controller.bean.ResultBody;
import com.chinasofti.ark.bdadp.service.tools.HbaseInfoService;
import com.chinasofti.ark.bdadp.util.hbase.common.HbaseQueryEntity;
import com.chinasofti.ark.bdadp.util.hbase.common.HbaseShellEntity;
import com.chinasofti.ark.bdadp.util.hbase.response.HbaseInfoRsp;
import com.chinasofti.ark.bdadp.util.hbase.response.HbaseQueryRsp;
import com.chinasofti.ark.bdadp.util.hbase.response.HbaseShellRsp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * Created by msc on 2016/9/27.
 */
@Controller
@RequestMapping(value = "/service/v1")
public class hbaseController {

    private static final Logger LOG = LoggerFactory.getLogger(hbaseController.class);

    @Resource
    private HbaseInfoService service;

    public static void main(String[] args) {
        hbaseController controller = new hbaseController();
        controller.testInitService();
        ResultBody en = controller.getHbaseInfos();
        HbaseInfoRsp rsp = (HbaseInfoRsp) en.getResult();
        System.out.println(">>> tableInfos: " + rsp);

//		Entity queryRsp = controller.queryForRsp("hbase_simple_table", "", "");
//		HbaseQueryRsp queryResp = (HbaseQueryRsp)queryRsp.getResponseData();
//		System.out.println(">>> queryRsp: " + queryResp);
    }

    /**
     * Entrance to redirect to relative html.
     */
    @RequestMapping(value = "/get/HbaseInfo", method = RequestMethod.GET)
    public String getHbaseInfo() {
        return "hbaseinfo";
    }

    /**
     * List all tables.
     */
    @RequestMapping(value = "/get/HbaseInfo/tables", method = RequestMethod.GET)
    @ResponseBody
    public ResultBody getHbaseInfos() {
        ResultBody body = new ResultBody();
        HbaseInfoRsp rsp = new HbaseInfoRsp();
        try {
            rsp = service.fetchHbaseTables();
        } catch (Exception e) {
            LOG.error("Fetch hbase tables error: ", e);
            body.setResultCode(1);
            body.setResultMessage("操作失败!" + e.getMessage());
            return body;
        }
        body.setResult(rsp);
        body.setResultCode(0);
        body.setResultMessage("操作成功!");
        return body;
    }

    public void testInitService() {
        this.service = new HbaseInfoService();
    }

    /**
     * Get table structure of the specified table
     */
    @RequestMapping(value = "/get/HbaseInfo/{htableName}", method = RequestMethod.GET)
    @ResponseBody
    public ResultBody getHTableInfo(@PathVariable String htableName) {
        ResultBody body = new ResultBody();
        HbaseInfoRsp rsp;
        try {
            rsp = service.fetchHTableInfo(htableName);
        } catch (Exception e) {
            LOG.error("Fetch hTable [{}] error: {}", htableName, e);
            body.setResultCode(1);
            body.setResultMessage("操作失败!" + e.getMessage());
            return body;
        }
        body.setResult(rsp);
        body.setResultCode(0);
        body.setResultMessage("操作成功!");
        return body;
    }

    /**
     * Query for response by the given startRow and endRow.
     */
    @RequestMapping(value = "/get/HbaseInfo/scan", method = RequestMethod.POST)
    @ResponseBody
    public ResultBody queryForRsp(@RequestBody HbaseQueryEntity request) {
        ResultBody body = new ResultBody();
        HbaseQueryRsp rsp;
        String htableName = null;
        String startRow = null;
        String endRow = null;
        try {
            htableName = request.getTablename();
            startRow = request.getStartkey();
            endRow = request.getEndkey();
            System.out.print(htableName + ">>>>>>" + startRow + ">>>>>>" + endRow);
            rsp = service.query(htableName, startRow, endRow);
        } catch (Exception e) {
            LOG.error("Fetch hTable [{}] error: {}", htableName, e);
            body.setResultCode(1);
            body.setResultMessage("操作失败!" + e.getMessage());
            return body;
        }
        body.setResult(rsp);
        body.setResultCode(0);
        body.setResultMessage("操作成功!");
        return body;
    }

    /**
     * List all tables.
     */
    @RequestMapping(value = "/get/HbaseInfo/shell", method = RequestMethod.POST)
    @ResponseBody
    public ResultBody queryByShell(@RequestBody HbaseShellEntity shell) {
        ResultBody body = new ResultBody();
        HbaseShellRsp rsp;
        rsp = this.service.queryByShell(shell);
        body.setResult(rsp);
        body.setResultCode(0);
        body.setResultMessage("操作成功!");
        return body;
    }
}
