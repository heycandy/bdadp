package com.chinasofti.ark.bdadp.controller.resources;

import com.chinasofti.ark.bdadp.controller.bean.ResultBody;
import com.chinasofti.ark.bdadp.service.components.ComponentService;
import com.chinasofti.ark.bdadp.util.common.UUID;
import com.google.common.collect.Maps;
import com.google.common.net.HttpHeaders;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by White on 2016/09/09.
 */
@Controller
@RequestMapping(value = "/service/v1")
public class ResourcesController {

    private final String RESOURCE_TYPE_ICON = "icon";
    private final String RESOURCE_CATE_SCENARIO = "scenario";
    private final String RESOURCE_CATE_COMPONENTS = "components";
    private final String FILENAME_SPLIT_CHARACTER = "#";
    private final int FILENAME_SPLIT_INDEX = 0;
    private final int CONTENT_TYPE_INDEX = 1;
    private final int FILENAME_TIMESTAMP_INDEX = 2;
    private final char CONTENT_TYPE_SRC_CHARACTER = '/';
    private final char CONTENT_TYPE_DEST_CHARACTER = '@';
    private final String CONTENT_TYPE_OCTET_STREAM = "application/octet-stream";
    private final String CONTENT_TYPE_IMAGE_PNG = "image/png";
    private final String CONTENT_TYPE_MULTIPART_FORM_DATA = "multipart/form-data";
    private final String ICON_SIZE_PARAMETER = "_";
    private final String ICON_SIZE_XS = "xs";
    private final String ICON_SIZE_XL = "xl";
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ComponentService componentService;

    @RequestMapping(value = "/resources/{cate}/{id}/{resType}", method = RequestMethod.GET)
    public void get(HttpServletRequest request, HttpServletResponse response,
                    @PathVariable String cate, @PathVariable String id,
                    @PathVariable String resType) {

        String s = String.format("resources/%s/%s/%s", cate, id, resType);
        String realPath = request.getSession().getServletContext().getRealPath(s);
        logger.debug(String.format("resource real path %s", realPath));

        InputStream in = null;
        OutputStream out = null;

        String contentType = CONTENT_TYPE_OCTET_STREAM;

        try {
            if (resType.equals(RESOURCE_TYPE_ICON)) {
                if (cate.equals(RESOURCE_CATE_SCENARIO)) {
                    File file = getIcon4Scenario(realPath, request.getParameterMap());
                    if (file != null) {
                        in = new FileInputStream(file);
                    }

                    contentType = CONTENT_TYPE_IMAGE_PNG;

                } else if (cate.equals(RESOURCE_CATE_COMPONENTS)) {

                    in = componentService.getComponentIcon(id, request.getParameter("param"));

                    contentType = CONTENT_TYPE_IMAGE_PNG;

                }

            } else {
                //throw new RuntimeException(String.format("resource type not supported for %s", resType));
                String filename = request.getParameter("name") + "." + resType;
                Path path = Paths.get(realPath, filename);

                in = Files.newInputStream(path);
                contentType = CONTENT_TYPE_MULTIPART_FORM_DATA;

                response.setHeader(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment;filename=" + URLEncoder.encode(filename, "utf-8"));
            }

            Assert.notNull(in, String.format("no match resource for %s", s));

            int length = in.available();

            response.setCharacterEncoding("utf-8");
            response.setContentLength(length);
            response.setContentType(contentType);

            out = response.getOutputStream();

            IOUtils.copy(in, out);

        } catch (Exception e) {
            logger.error(s, e);
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    logger.error(s, e);
                }
            }

            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    logger.error(s, e);
                }
            }
        }
    }

    @ResponseBody
    @RequestMapping(value = "/resources/{cate}/{id}/{resType}", method = RequestMethod.POST)
    public ResultBody post(HttpServletRequest request, HttpServletResponse response,
                           @PathVariable String cate, @PathVariable String id,
                           @PathVariable String resType) {
        ResultBody<Object> body = new ResultBody<>();

        if (id.equals("*")) {
            id = UUID.getId();
        }
        String s = String.format("resources/%s/%s/%s", cate, id, resType);
        String realPath = request.getSession().getServletContext().getRealPath(s);

        File dir = new File(realPath);
        if (!dir.exists()) {
            Assert.isTrue(dir.mkdirs(), "resource path not exists and make dir failure.");
        }

        try {
            CommonsMultipartResolver
                    multipartResolver =
                    new CommonsMultipartResolver(request.getSession().getServletContext());
            if (multipartResolver.isMultipart(request)) {
                MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
                Iterator<String> iterator = multiRequest.getFileNames();
                while (iterator.hasNext()) {
                    MultipartFile file = multiRequest.getFile(iterator.next());
                    if (file != null && !file.isEmpty()) {
                        String originalFilename = file.getOriginalFilename();

                        File localFile;

                        if (cate.equals(RESOURCE_CATE_SCENARIO) && resType.equals("zip")) {
                            localFile = new File(realPath, originalFilename);

                        } else {
                            //image/png image@png
                            String
                                    contentType =
                                    file.getContentType()
                                            .replace(CONTENT_TYPE_SRC_CHARACTER, CONTENT_TYPE_DEST_CHARACTER);

                            //originalFilename&contentType&timestamp
                            String
                                    child =
                                    String
                                            .format("%s%s%s%s%d", originalFilename, FILENAME_SPLIT_CHARACTER, contentType,
                                                    FILENAME_SPLIT_CHARACTER, System.currentTimeMillis());

                            localFile = new File(realPath, child);
                        }

                        logger.debug(String.format("%s transferTo %s", originalFilename, localFile));

                        file.transferTo(localFile);

                        String filename = localFile.getName();
                        Map<String, String> map = Maps.newHashMap();

                        int lastIndex = filename.lastIndexOf(".");
                        if (lastIndex != -1) {
                            String name = filename.substring(0, lastIndex);
                            String res0 = filename.substring(lastIndex + 1, filename.length());

                            map.put("id", id);
                            map.put("name", name);
                            map.put("resType", res0);

                        } else {
                            map.put("id", id);
                            map.put("name", filename);
                            map.put("resType", "");
                        }

                        body.setResult(map);
                    }
                }
            }

        } catch (Exception e) {
            logger.error(s, e);
            body.setResultCode(1);
            body.setResultMessage(e.getMessage());
        }

        return body;
    }

    @ResponseBody
    @RequestMapping(value = "/resources/{cate}/{id}/{resType}", method = RequestMethod.DELETE)
    public ResultBody delete(HttpServletRequest request, HttpServletResponse response,
                             @PathVariable String cate, @PathVariable String id,
                             @PathVariable String resType) {
        ResultBody<Object> body = new ResultBody<>();

        String s = String.format("resources/%s/%s/%s", cate, id, resType);
        String realPath = request.getSession().getServletContext().getRealPath(s);
        String pathname = request.getParameter("name") + "." + resType;

        try {
            body.setResult(
                    new File(realPath, pathname).delete() &&
                            new File(realPath).delete());

        } catch (Exception e) {
            logger.error(s, e);
            body.setResultCode(1);
            body.setResultMessage(e.getMessage());
        }

        return body;

    }

    private File getIcon4Scenario(String realPath, Map map) {
        File dir = new File(realPath);
        if (!dir.exists()) {
            return null;
        }
//    Assert.isTrue(dir.exists(), String.format("resource path exists for %s", realPath));

        File[] files = dir.listFiles();
        Assert.notNull(files, String.format("no match resource file for %s", dir));

        Arrays.sort(files, new Comparator<File>() {
            @Override
            public int compare(File o1, File o2) {
                String[] o1NameSplit = o1.getName().split(FILENAME_SPLIT_CHARACTER);
                String[] o2NameSplit = o2.getName().split(FILENAME_SPLIT_CHARACTER);
                Assert.isTrue(o1NameSplit.length == 3,
                        String.format("file %s name is incorrect.", o1.getName()));
                Assert.isTrue(o2NameSplit.length == 3,
                        String.format("file %s name is incorrect.", o2.getName()));

                long t1 = Long.valueOf(o1NameSplit[FILENAME_TIMESTAMP_INDEX]);
                long t2 = Long.valueOf(o2NameSplit[FILENAME_TIMESTAMP_INDEX]);

                return (int) (t2 - t1);
            }
        });

        return files[0];
    }

    private File getIcon4Components(String realPath, Map map) {
        File dir = new File(realPath);
        Assert.isTrue(dir.exists(), String.format("resource path exists for %s", realPath));

        String size = map.get(ICON_SIZE_PARAMETER) != null ?
                ((String[]) map.get(ICON_SIZE_PARAMETER))[0] : ICON_SIZE_XL;

        File[] files = dir.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                String name = pathname.getName();
                String[] nameSplit = name.split(FILENAME_SPLIT_CHARACTER);
                Assert.isTrue(nameSplit.length == 3, String.format("file %s name is incorrect.", name));

                return nameSplit[FILENAME_SPLIT_INDEX].contains(size);
            }
        });

        Assert.notNull(files, String.format("no match resource file for %s", dir));

        Arrays.sort(files, new Comparator<File>() {
            @Override
            public int compare(File o1, File o2) {
                String[] o1NameSplit = o1.getName().split(FILENAME_SPLIT_CHARACTER);
                String[] o2NameSplit = o2.getName().split(FILENAME_SPLIT_CHARACTER);
                Assert.isTrue(o1NameSplit.length == 3,
                        String.format("file %s name is incorrect.", o1.getName()));
                Assert.isTrue(o2NameSplit.length == 3,
                        String.format("file %s name is incorrect.", o2.getName()));

                long t1 = Long.valueOf(o1NameSplit[FILENAME_TIMESTAMP_INDEX]);
                long t2 = Long.valueOf(o2NameSplit[FILENAME_TIMESTAMP_INDEX]);

                return (int) (t2 - t1);
            }
        });

        return files[0];
    }

}
