package com.chinasofti.ark.bdadp.component.api.data;

public abstract class Data<T> {

    private T rawData;

    public Data(T rawData) {
        this.rawData = rawData;
    }

    public T getRawData() {
        return this.rawData;
    }

    public abstract DataType getType();

    public abstract DataInfo<?> getInfo();
}
