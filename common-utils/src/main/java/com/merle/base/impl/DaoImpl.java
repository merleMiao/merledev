package com.merle.base.impl;

import com.merle.base.Dao;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * Created by 少康 on 2016/5/27.
 */
@Service
public class DaoImpl<T> implements Dao<T> {

    @Resource
    protected SqlSession sqlsession;

    protected String getIface(){
        return this.getClass().getInterfaces()[0].getName();
    }

    @Override
    public int save(T t) {
        return this.sqlsession.insert(this.getIface() + ".save", t);
    }

    @Override
    public T finByUuid(String uuid) {
        return this.sqlsession.selectOne(this.getIface() + ".findByUuid", uuid);
    }

    @Override
    public List<T> list(Map map) {

        return this.sqlsession.selectList(this.getIface() + ".list", map);
    }

    @Override
    public int count(Map map) {
        return this.sqlsession.selectOne(this.getIface() + ".count", map);
    }

    @Override
    public int delete(Map map) {
        return this.sqlsession.delete(this.getIface() + ".delete", map);
    }

    @Override
    public int update(T t) {
        return this.sqlsession.delete(this.getIface() + ".delete", t);
    }
}
