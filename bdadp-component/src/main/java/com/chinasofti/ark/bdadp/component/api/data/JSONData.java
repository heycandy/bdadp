package com.chinasofti.ark.bdadp.component.api.data;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * Created by White on 2017/1/20.
 */
public class JSONData extends Data<JsonNode> {

    public JSONData(JsonNode rawData) {
        super(rawData);
    }

    @Override
    public DataType getType() {
        return DataType.JSON;
    }

    @Override
    public DataInfo<?> getInfo() {
        return null;
    }
}
