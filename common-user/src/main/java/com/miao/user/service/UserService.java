package com.miao.user.service;


import com.miao.user.pojo.User;

public interface UserService {

    public int save(User user);

    public User findByUuid(String uuid);

    public User findById(int id);
}
