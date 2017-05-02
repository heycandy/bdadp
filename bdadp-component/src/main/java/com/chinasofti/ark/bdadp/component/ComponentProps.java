package com.chinasofti.ark.bdadp.component;

import com.google.common.base.Strings;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by White on 2016/09/04.
 */
public class ComponentProps {

    private Map<String, String> properties = new HashMap<>();

    public String getString(String key) {
        return properties.get(key);
    }

    public String getString(String key, String defaultValue) {
        String s = getString(key);

        return Strings.isNullOrEmpty(s) ? defaultValue : s;
    }

    public int getInt(String key) {
        String s = getString(key);

        return Integer.parseInt(s);
    }

    public int getInt(String key, int defaultValue) {
        String s = getString(key);

        return Strings.isNullOrEmpty(s) ? defaultValue : Integer.valueOf(s);
    }

    public void setProperty(String key, String value) {
        properties.put(key, value);
    }

    public Set<String> getKeySet() {
        return properties.keySet();
    }

    public void copy(ComponentProps props) {
        for (String s : props.getKeySet()) {
            properties.put(s, props.getString(s));
        }
    }

    public boolean isEmpty() {
        return this.properties.isEmpty();
    }
}
