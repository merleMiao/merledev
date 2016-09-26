package com.miao.user.handler;

import com.merle.io.JsonUtils;
import com.merle.rmq.AbsHandler;
import com.miao.listener.CommonListener;
import com.miao.user.pojo.User;
import com.miao.user.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by 少康 on 2016/6/1.
 */
@Component
public class TestHandler extends AbsHandler {

    @Autowired
    UserService userService;

    private Logger logger = LoggerFactory.getLogger(TestHandler.class);

    @Override
    public boolean process(String message) {
        userService = CommonListener.ac.getBean(UserService.class);
        User user = userService.findById(1000);
        logger.info("message:" + message);
        logger.info("message:user:" + JsonUtils.toJson(user));
        return false;
    }
}
