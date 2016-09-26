package com.miao.user.service.impl;

import com.miao.user.dao.UserDao;
import com.miao.user.pojo.User;
import com.miao.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserDao userDao;

    @Override
    public User findByUuid(String uuid) {
        return userDao.finByUuid(uuid);
    }

    @Override
    public int save(User user) {
        return userDao.save(user);
    }

    @Override
    public User findById(int id) {
        return userDao.findById(id);
    }
}
