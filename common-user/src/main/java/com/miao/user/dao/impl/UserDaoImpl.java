package com.miao.user.dao.impl;

import com.merle.base.Dao;
import com.merle.base.impl.DaoImpl;
import com.miao.user.dao.UserDao;
import com.miao.user.pojo.User;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UserDaoImpl extends DaoImpl<User> implements UserDao {

    @Override
    public User findById(int id) {
        return this.sqlsession.selectOne(this.getIface() + ".findById", id);
    }
}
