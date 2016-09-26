package com.miao.listener;

import com.merle.rmq.SubscriberUtils;
import com.miao.user.handler.TestHandler;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContextEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CommonListener extends ContextLoaderListener {

    public static WebApplicationContext ac;

    private static List<Map> apis = new ArrayList();

    @Override
    public void contextInitialized(ServletContextEvent event) {
        super.contextInitialized(event);
        System.out.println("init xxListner");
        ac = WebApplicationContextUtils.getRequiredWebApplicationContext(event.getServletContext());
        //订阅评论事件
        SubscriberUtils.addTopicListener("common-web-topic.test", "common-web-topic.test-queue", new TestHandler());

    }

}