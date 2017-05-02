package com.chinasofti.ark.bdadp.service.push.bean;

import java.util.Collections;

/**
 * Created by White on 2016/09/13.
 */
public class EventBody {

    private int code;
    private String name;
    private Object result;
    private Iterable<String> tokens;

    public EventBody() {
    }

    public EventBody(int code, String name, Object result) {
        this(code, name, result, Collections.emptySet());
    }

    public EventBody(int code, String name, Object result, Iterable<String> tokens) {
        this.code = code;
        this.name = name;
        this.result = result;
        this.tokens = tokens;
    }

    public int getCode() {
        return code;
    }

    public EventBody setCode(int code) {
        this.code = code;
        return this;
    }

    public String getName() {
        return name;
    }

    public EventBody setName(String name) {
        this.name = name;
        return this;
    }

    public Object getResult() {
        return result;
    }

    public EventBody setResult(Object result) {
        this.result = result;
        return this;
    }

    public Iterable<String> getTokens() {
        return tokens;
    }

    public EventBody setTokens(Iterable<String> tokens) {
        this.tokens = tokens;
        return this;
    }

    @Override
    public String toString() {
        return "EventBody{" +
                "code=" + code +
                ", name='" + name + '\'' +
                ", result=" + result +
                ", tokens='" + tokens + '\'' +
                '}';
    }
}
