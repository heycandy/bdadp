package com.chinasofti.ark.bdadp.component.api.data;

import org.dom4j.Document;

/**
 * Created by White on 2017/1/17.
 */
public class XMLData extends Data<Document> {

    public XMLData(Document rawData) {
        super(rawData);
    }

    @Override
    public DataType getType() {
        return DataType.XML;
    }

    @Override
    public DataInfo<?> getInfo() {
        return null;
    }
}
