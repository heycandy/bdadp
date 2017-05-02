package com.chinasofti.ark.bdadp.component.api;

import com.chinasofti.ark.bdadp.component.api.data.Data;

public interface DataCacheable<T> {

    public void cache(Data<T> data);

}
