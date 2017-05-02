package com.chinasofti.ark.bdadp.controller.tools;

import com.chinasofti.ark.bdadp.controller.bean.ResultBody;
import com.chinasofti.ark.bdadp.service.queue.QueueTaskService;
import com.chinasofti.ark.bdadp.service.tools.HdfsInfoService;
import com.chinasofti.ark.bdadp.service.tools.bean.HdfsDownLoadTask;
import com.chinasofti.ark.bdadp.util.common.UUID;
import com.chinasofti.ark.bdadp.util.process.ArkBdadpProcess;
import com.chinasofti.ark.bdadp.util.process.ArkBdadpProcessBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by msc on 2016/9/18.
 */
@Controller
@RequestMapping(value = "/service/v1")
public class HdfsController {

    @Autowired
    private HdfsInfoService service;

    @Autowired
    private QueueTaskService queueTaskService;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @RequestMapping(value = "/get/HdfsInfo", method = RequestMethod.GET)
    @ResponseBody
    public ResultBody getHdfsList(HttpServletRequest request) {
        ResultBody body = new ResultBody();
        Map<String, Boolean> rsp = new HashMap<String, Boolean>();
        try {
//      String target = (String)JSONObject.parseObject(req).get("a");
            String path = request.getParameter("path");
            rsp = service.listFiles(path);
            System.out.println(rsp);
        } catch (Exception e) {
            logger.error("Get HdfsInfo error: ", e);
            body.setResultCode(1);
            body.setResultMessage("操作失败!" + e.getMessage());
            return body;
        }
        body.setResult(rsp);
        body.setResultCode(0);
        body.setResultMessage("操作成功!");
        return body;
    }

    @ResponseBody
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public Map<String, String> fileUpload(@RequestParam("file") CommonsMultipartFile file,
                                          HttpServletRequest req)
            throws IOException {
        String message = "0";
        // 获取上传文件路径
        File upload = new File(req.getParameter("filePath"), req.getParameter("fileName"));
        InputStream is = null;
        try {
            is = file.getInputStream();
            service.uploadFile(is, upload.getPath());
            logger.info("File[{}] to hdfs[{}] uploading successful!", upload.getPath());
        } catch (Exception e) {
            message = "1";
        } finally {
            if (is != null) {
                is.close();
            }
        }
        Map<String, String> map = new HashMap<String, String>();
        map.put("status", message);
        System.out.println(req.getParameter("fileName"));
        return map;
    }

    @ResponseBody
    @RequestMapping(value = "/read/hdfsFile", method = RequestMethod.POST)
    public ResultBody readFile(@RequestBody Map reqBody, HttpServletRequest req) {
        ResultBody body = new ResultBody();
        String message = null;
        try {
            message = service.readFile(reqBody.get("path").toString());
            body.setResult(message);
            body.setResultCode(0);
            body.setResultMessage("excute success!");
        } catch (IOException e) {
            logger.error("Read HdfsFile error: ", e);
            body.setResultCode(1);
            body.setResultMessage("Read HdfsFile error:" + e.getMessage());
        }
        return body;
    }

    @ResponseBody
    @RequestMapping(value = "/delete/hdfsFile", method = RequestMethod.DELETE)
    public ResultBody delHDFSFile(@RequestBody Map reqBody, HttpServletRequest req) {
        ResultBody body = new ResultBody();
        //String path = req.getParameter("path");
        System.out.print(reqBody.get("path"));
        try {
            service.delFile(reqBody.get("path").toString());
        } catch (Exception e) {
            body.setResult(false);
            body.setResultCode(1);
            body.setResultMessage("操作失败!" + e);
            return body;
        }
        body.setResult(true);
        body.setResultCode(0);
        body.setResultMessage("操作成功!");
        return body;
    }

    @RequestMapping(value = "/downLoad/hdfsFile", method = RequestMethod.POST)
    @ResponseBody
    public ResultBody downLoad(@RequestBody Map reqBody, HttpServletRequest request) {
        ResultBody<Object> body = new ResultBody<>();
        String id0 = UUID.getId();
        //String downLoadPath = reqBody.get("downLoadPath").toString();
        String filePath = reqBody.get("filePath").toString();
        String name = filePath.substring(filePath.lastIndexOf("/") + 1, filePath.length());
        String s0 = String.format("resources/tools/%s/%s", id0, "zip");
        String realPath0 = request.getSession().getServletContext().getRealPath(s0);
        logger.debug(String.format("resource real path %s", realPath0));
        try {
            Files.createDirectories(Paths.get(realPath0));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Path path0 = Paths.get(realPath0, name + ".zip");
        queueTaskService.submit(new HdfsDownLoadTask(id0, name, path0, filePath));
        body.setResult(id0);
        body.setResultCode(0);
        return body;
    }

    @RequestMapping(value = "/exec/command", method = RequestMethod.GET)
    @ResponseBody
    public ResultBody execCommand(HttpServletRequest request) {
        ResultBody body = new ResultBody();
        Map<String, Object> rsp = new HashMap<>();

        String prog = request.getParameter("prog");
        String command = request.getParameter("command");
        String env = request.getParameter("env");
        String
                workingDir =
                request.getParameter("workingDir") != null ? request.getParameter("workingDir")
                        : System.getProperty("user.dir");
        ArkBdadpProcessBuilder builder = new ArkBdadpProcessBuilder(command.split(" "))
                .setEnv(System.getenv())
                .setWorkingDir(workingDir);

        logger.info("Environment variables: " + builder.getEnv());
        logger.info("Working directory: " + builder.getWorkingDir());

        long startMs = System.currentTimeMillis();
        boolean success = false;
        ArkBdadpProcess process = builder.build();
        try {
            process.run();
            success = true;
        } catch (Exception e) {
            logger.error("Exec command error..", e);
            body.setResultCode(1);
            body.setResultMessage("操作失败!" + e.getMessage());
            return body;
        } finally {
            logger.info("Process completed " + (success ? "successfully" : "unsuccessfully") + " in "
                    + ((System.currentTimeMillis() - startMs) / 1000) + " seconds.");
        }

        rsp.put("outputLog", process.getOutputGobbler().getRecentLog());
        rsp.put("errorLog", process.getErrorGobbler().getRecentLog());

        body.setResult(rsp);
        body.setResultCode(0);
        body.setResultMessage("操作成功!");
        return body;
    }
}
