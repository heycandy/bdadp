package com.chinasofti.ark.bdadp.security;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SecurityConfig {

    private Map<String, Object> configs = new ConcurrentHashMap<String, Object>();

    public Object put(String param, Object val) {
        return this.configs.put(param, val);
    }

    public Object get(String param) {
        return this.configs.get(param);
    }

}
