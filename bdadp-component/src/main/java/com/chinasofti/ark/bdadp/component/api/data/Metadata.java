package com.chinasofti.ark.bdadp.component.api.data;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Metadata {

    private Map<String, Field> fields = new HashMap<String, Field>();

    public Metadata() {

    }

    public Metadata addField(Field field) {
        this.fields.put(field.getName(), field);
        return this;
    }

    public Metadata addStrField(String name) {
        this.fields
                .put(name, new Field(name, com.chinasofti.ark.bdadp.component.api.data.FieldType.STR));
        return this;
    }

    public Metadata addNumField(String name) {
        this.fields.put(name, new Field(name, FieldType.NUM));
        return this;
    }

    public Metadata addDateField(String name) {
        this.fields.put(name, new Field(name, FieldType.DATE));
        return this;
    }

    public Metadata addTimeField(String name) {
        this.fields.put(name, new Field(name, FieldType.TIME));
        return this;
    }

    public Metadata addDateTimeField(String name) {
        this.fields.put(name, new Field(name, FieldType.DATETIME));
        return this;
    }

    public Metadata addListField(String name) {
        this.fields.put(name, new Field(name, FieldType.LIST));
        return this;
    }

    public Metadata addJsonField(String name) {
        this.fields.put(name, new Field(name, FieldType.JSON));
        return this;
    }

    public Metadata addXmlField(String name) {
        this.fields.put(name, new Field(name, FieldType.XML));
        return this;
    }

    public Collection<Field> fields() {
        return this.fields.values();
    }

    public Field getField(String name) {
        return this.fields.get(name);
    }

    public int size() {
        return this.fields.size();
    }

}
