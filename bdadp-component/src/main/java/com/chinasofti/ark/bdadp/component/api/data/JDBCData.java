package com.chinasofti.ark.bdadp.component.api.data;

import java.sql.Connection;

/**
 * Created by White on 2017/1/11.
 */
public class JDBCData extends Data<Connection> {

    public JDBCData(Connection rawData) {
        super(rawData);
    }

    @Override
    public DataType getType() {
        return DataType.JDBC;
    }

    @Override
    public DataInfo<?> getInfo() {
        return null;
    }

}
