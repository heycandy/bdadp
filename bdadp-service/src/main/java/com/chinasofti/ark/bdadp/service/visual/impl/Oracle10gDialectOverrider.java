package com.chinasofti.ark.bdadp.service.visual.impl;

import org.hibernate.dialect.Oracle10gDialect;
import org.hibernate.type.StandardBasicTypes;

import java.sql.Types;

/**
 * Created by Hu on 2017/5/8.
 */
public class Oracle10gDialectOverrider extends Oracle10gDialect {
    public Oracle10gDialectOverrider() {
        super();
        registerHibernateType(Types.NVARCHAR, StandardBasicTypes.STRING.getName());
    }
}
