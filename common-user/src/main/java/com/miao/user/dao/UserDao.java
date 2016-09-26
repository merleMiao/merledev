package com.miao.user.dao;


import com.merle.base.Dao;
import com.miao.user.pojo.User;

public interface UserDao extends Dao<User>{

    public User findById(int id);
}
