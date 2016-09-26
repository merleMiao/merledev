package com.miao.user.controller;

import com.merle.exception.MyException;
import com.merle.io.RequestUtils;
import com.merle.rmq.SubscriberUtils;
import com.miao.user.pojo.User;
import com.miao.user.service.UserService;
import org.apache.commons.lang.StringUtils;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.SolrInputField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;


@Controller
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    HttpSolrClient httpSolrClient;

    @RequestMapping(name = "用户列表", value = "/user/list", method = RequestMethod.GET)
    public Object list(HttpServletRequest request){
        ModelAndView modelAndView = new ModelAndView();
        List<User> userList = new ArrayList<User>();
        User user = new User();
        user.setName("小明");
        userList.add(user);
        User user1 = new User();
        user1.setName("小红");
        userList.add(user1);
        SubscriberUtils.sendMessage(userList, "common-web-topic.test");
        modelAndView.addObject("list", userList);
        modelAndView.addObject("result", 0);
        modelAndView.addObject("msg", "成功");
        return modelAndView;
    }

    @RequestMapping(name = "用户详情", value = "/user/detail/{uuid}", method = RequestMethod.GET)
    public Object detail(HttpServletRequest request, @PathVariable String uuid){
        ModelAndView modelAndView = new ModelAndView();
        User user = userService.findByUuid(uuid);
        modelAndView.addObject("user", user);
        modelAndView.addObject("result", 0);
        modelAndView.addObject("msg", "成功");
        return modelAndView;
    }

    @RequestMapping(name = "用户注册", value = "/user", method = RequestMethod.POST)
    public Object save(HttpServletRequest request) throws Exception {
        ModelAndView modelAndView = new ModelAndView();
        RequestUtils req = new RequestUtils(request);
        User user = req.create(User.class);
        userService.save(user);
        User _user = userService.findByUuid(user.getUuid());
        modelAndView.addObject("user", _user);
        modelAndView.addObject("result", 0);
        modelAndView.addObject("msg", "成功");
        return modelAndView;
    }

    @RequestMapping(name = "用户登录", value = "/user/login", method = RequestMethod.POST)
    public Object login(HttpServletRequest request) throws Exception {
        ModelAndView modelAndView = new ModelAndView();
        RequestUtils req = new RequestUtils(request);
        int id = req.getInt("id");
        String password = req.getParameter("password");
        User user = userService.findById(id);
        if(!StringUtils.equalsIgnoreCase(user.getPassword(), password)){
            throw new MyException("用户名或者密码不正确");
        }
        modelAndView.addObject("result", 0);
        modelAndView.addObject("msg", "成功");
        return modelAndView;
    }

    @RequestMapping(name = "solr测试", value = "/solr/test}", method = RequestMethod.GET)
    public Object test(HttpServletRequest request) throws IOException, SolrServerException {
        ModelAndView modelAndView = new ModelAndView();
        SolrInputDocument document = new SolrInputDocument();
        SolrInputField solrInputField = new SolrInputField("test");
        solrInputField.addValue("testKey", 123);
        document.put("test", solrInputField);
        httpSolrClient.add(document);
        modelAndView.addObject("result", 0);
        modelAndView.addObject("msg", "成功");
        return modelAndView;
    }


}
