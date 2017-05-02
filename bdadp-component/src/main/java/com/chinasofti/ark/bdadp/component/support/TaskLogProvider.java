package com.chinasofti.ark.bdadp.component.support;

import org.apache.log4j.*;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;

/**
 * Created by White on 2016/09/25.
 */
public class TaskLogProvider {

    public static final String DEFAULT_FILE_CHILD = "execution";
    private static final Logger logger = Logger.getLogger(TaskLogProvider.class);
    private static final String DEFAULT_NAME_FORMAT = "%s_%s.out";
    private static final String DEFAULT_APPENDER_NAME = "D";

    public static org.slf4j.Logger getLog(String taskId, String executionId) throws IOException {
        String name = getName(taskId, executionId);

        Logger log = Logger.getLogger(name);

        Layout layout = getLayout();
        String filename = getFilename(taskId, executionId, DEFAULT_FILE_CHILD);

        Appender newAppender = new FileAppender(layout, filename);

        log.addAppender(newAppender);

        return LoggerFactory.getLogger(name);
    }

    public static String getName(String taskId, String executionId) {
        return String.format(DEFAULT_NAME_FORMAT, taskId, executionId);
    }

    public static String getFilename(String taskId, String executionId, String child) {
        File execution = new File(getParent(), child);
        File parent = new File(execution, executionId);
        File file = new File(parent, taskId);

        return file.getPath();
    }

    public static void close(org.slf4j.Logger log) {
        String name = log.getName();

        Logger logger = Logger.getLogger(name);

        Enumeration enumeration = logger.getAllAppenders();
        while (enumeration != null && enumeration.hasMoreElements()) {
            Object obj = enumeration.nextElement();
            if (obj instanceof Appender) {
                ((Appender) obj).close();
            }
        }

        logger.removeAllAppenders();

    }

    private static String getParent() {
        Appender appender = logger.getParent().getAppender(DEFAULT_APPENDER_NAME);

        String file = "";
        if (appender != null && appender instanceof FileAppender) {
            file = ((FileAppender) appender).getFile();
        } else {
            Enumeration enumeration = logger.getParent().getAllAppenders();
            while (enumeration != null && enumeration.hasMoreElements()) {
                Object obj = enumeration.nextElement();
                if (obj instanceof FileAppender) {
                    file = ((FileAppender) obj).getFile();
                    break;
                }
            }
        }

        return new File(file).getParent();
    }

    private static Layout getLayout() {
        Appender appender = logger.getParent().getAppender(DEFAULT_APPENDER_NAME);

        Layout layout = null;
        if (appender != null && appender instanceof FileAppender) {
            layout = appender.getLayout();
        } else {
            Enumeration enumeration = logger.getParent().getAllAppenders();
            while (enumeration.hasMoreElements()) {
                Object obj = enumeration.nextElement();
                if (obj instanceof FileAppender) {
                    layout = ((FileAppender) obj).getLayout();
                    break;
                }
            }
        }

        if (layout == null) {
            layout = new PatternLayout();
        }

        return layout;
    }
}
