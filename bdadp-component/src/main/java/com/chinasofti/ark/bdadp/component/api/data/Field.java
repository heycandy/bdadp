package com.chinasofti.ark.bdadp.component.api.data;

public class Field {

    private final String name;
    private final com.chinasofti.ark.bdadp.component.api.data.FieldType type;

    public Field(String name, FieldType type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return this.name;
    }

    public FieldType getType() {
        return this.type;
    }

}
