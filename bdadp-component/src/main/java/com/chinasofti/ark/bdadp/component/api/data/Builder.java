package com.chinasofti.ark.bdadp.component.api.data;

import com.fasterxml.jackson.databind.JsonNode;
import org.apache.spark.sql.DataFrame;
import org.dom4j.Document;

import java.io.File;
import java.io.InputStream;
import java.sql.Connection;

/**
 * Created by White on 2017/1/6.
 */
public class Builder {

    public static FileData build(File rawData) {
        return new FileData(rawData);
    }

    public static JDBCData build(Connection rawData) {
        return new JDBCData(rawData);
    }

    public static JSONData build(JsonNode rawData) {
        return new JSONData(rawData);
    }

    public static SparkData build(DataFrame rawData) {
        return new SparkData(rawData);
    }

    public static StreamData build(InputStream rawData) {
        return new StreamData(rawData);
    }

    public static StringData build(String rawData) {
        return new StringData(rawData);
    }

    public static XMLData build(Document rawData) {
        return new XMLData(rawData);
    }
}
