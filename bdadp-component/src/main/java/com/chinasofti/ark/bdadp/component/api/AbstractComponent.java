package com.chinasofti.ark.bdadp.component.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.slf4j.Logger;

import java.io.StringWriter;
import java.util.Map;

/**
 * Created by White on 2016/09/05.
 */
public abstract class AbstractComponent implements Component {

    private final String _id;
    private final String _name;
    private final Logger _log;
    protected volatile double _progress;

    protected AbstractComponent(String id, String name, Logger log) {
        _id = id;
        _name = name;
        _log = log;

        _progress = 0.0;
    }

    @Override
    public String getId() {
        return this._id;
    }

    @Override
    public String getName() {
        return this._name;
    }

    @Override
    public double getProgress() throws Exception {
        return this._progress;
    }

    public Logger getLog() {
        return this._log;
    }

    public void debug(String message) {
        this._log.debug(message);
    }

    public void debug(String message, Throwable t) {
        this._log.debug(message, t);
    }

    public void info(String message) {
        this._log.info(message);
    }

    public void info(String message, Throwable t) {
        this._log.info(message, t);
    }

    public void warn(String message) {
        this._log.warn(message);
    }

    public void warn(String message, Throwable t) {
        this._log.warn(message, t);
    }

    public void error(String message) {
        this._log.error(message);
    }

    public void error(String message, Throwable t) {
        this._log.error(message, t);
    }

    public void chart(String templateName, Map<String, Object> entrySet) {
        VelocityEngine ve = new VelocityEngine();

        ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
        ve.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
        ve.init();

        Template template = ve.getTemplate(templateName);
        VelocityContext ctx = new VelocityContext();
        StringWriter sw = new StringWriter();

        ObjectMapper mapper = new ObjectMapper();
        for (Map.Entry<String, Object> entry : entrySet.entrySet()) {
            try {
                String json = mapper.writeValueAsString(entry.getValue());
                ctx.put(entry.getKey(), json);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }

        }

        template.merge(ctx, sw);

        System.out.println(sw.toString());
        this._log.info(sw.toString());
    }

    @Override
    public String toString() {
        return "ArkAbstractComponent{" +
                "_id='" + _id + '\'' +
                ", _name='" + _name + '\'' +
                ", _progress=" + _progress +
                '}';
    }
}
