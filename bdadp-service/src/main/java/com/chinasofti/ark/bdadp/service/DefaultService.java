package com.chinasofti.ark.bdadp.service;

/**
 * Created by White on 2016/09/01.
 */
public interface DefaultService<S, T> {

    public S create(S s);

    public Iterable<S> create(Iterable<S> iterable);

    public void delete(T id);

    public S findOne(T id);

    public Iterable<S> findAll();

    public S update(S s);

    public Iterable<S> update(Iterable<S> iterable);
}
