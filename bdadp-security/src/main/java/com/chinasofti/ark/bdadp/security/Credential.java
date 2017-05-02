package com.chinasofti.ark.bdadp.security;

public class Credential<T> {

    private final T content;

    public Credential(T content) {
        this.content = content;
    }

    public T getContent() {
        return this.content;
    }

    public String toString() {
        return this.content.toString();
    }
}
