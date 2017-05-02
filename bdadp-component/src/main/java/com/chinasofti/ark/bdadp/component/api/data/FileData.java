package com.chinasofti.ark.bdadp.component.api.data;

import java.io.File;

/**
 * Created by White on 2017/1/11.
 */
public class FileData extends Data<File> {

    public FileData(File rawData) {
        super(rawData);
    }

    @Override
    public DataType getType() {
        return DataType.FILE;
    }

    @Override
    public DataInfo<?> getInfo() {
        return null;
    }
}
