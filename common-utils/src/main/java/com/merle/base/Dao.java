package com.merle.base;

import java.util.List;
import java.util.Map;

/**
 * Created by 少康 on 2016/5/27.
 */
public interface Dao<T> {

    public int save(T t);

    public T finByUuid(String uuid);

    public List<T> list(Map map);

    public int count(Map map);

    public int delete(Map map);

    public int update(T t);

}
