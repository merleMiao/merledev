package com.miao.quartz.controller;

import com.alibaba.druid.util.StringUtils;
import com.merle.io.JsonUtils;
import com.merle.io.RequestUtils;
import com.merle.rmq.SubscriberUtils;
import com.miao.quartz.service.JobService;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by 少康 on 2016/6/2.
 */
@Controller
public class JobController {

    @Autowired
    JobService jobService;

    @RequestMapping(name = "添加定时任务(或修改)", value = "/job", method = RequestMethod.POST)
    public Object addJob(HttpServletRequest request){
        ModelAndView modelAndView = new ModelAndView();
        RequestUtils requestUtils = new RequestUtils(request);
        String jobName = requestUtils.getParameter("job_name");
        String jobGroup = requestUtils.getParameter("job_group");
        String jobDesc = requestUtils.getParameter("job_desc");
        String jobData = requestUtils.getParameter("job_data");
        Map jobDataMap = JsonUtils.toBean(jobData, Map.class);
        String cronExpression = requestUtils.getParameter("cron_expression");
        long startTime = requestUtils.getLong("start_time", new Date().getTime());
        long endTime = requestUtils.getLong("end_time", new Date().getTime() + 24*3600);
        jobService.addJob(jobName, jobGroup, jobDesc, jobDataMap, cronExpression, startTime, endTime);
        modelAndView.addObject("result", 0);
        modelAndView.addObject("msg", "成功");
        return modelAndView;
    }

    @RequestMapping(name = "暂停定时任务", value = "/job/pause", method = RequestMethod.GET)
    public Object pauseJob(HttpServletRequest request) throws SchedulerException {
        ModelAndView modelAndView = new ModelAndView();
        RequestUtils requestUtils = new RequestUtils(request);
        String jobName = requestUtils.getParameter("job_name");
        String jobGroup = requestUtils.getParameter("job_group");
        jobService.pauseJob(jobName, jobGroup);
        modelAndView.addObject("result", 0);
        modelAndView.addObject("msg", "成功");
        return modelAndView;
    }

    @RequestMapping(name = "重启定时任务", value = "/job/resume", method = RequestMethod.GET)
    public Object resumeJob(HttpServletRequest request) throws SchedulerException {
        ModelAndView modelAndView = new ModelAndView();
        RequestUtils requestUtils = new RequestUtils(request);
        String jobName = requestUtils.getParameter("job_name");
        String jobGroup = requestUtils.getParameter("job_group");
        jobService.resumeJob(jobName, jobGroup);
        modelAndView.addObject("result", 0);
        modelAndView.addObject("msg", "成功");
        return modelAndView;
    }

    @RequestMapping(name = "定时任务列表", value = "/job/list", method = RequestMethod.GET)
    public Object listJob(HttpServletRequest request) throws SchedulerException {
        ModelAndView modelAndView = new ModelAndView();
        RequestUtils requestUtils = new RequestUtils(request);
        String status = requestUtils.getParameter("status");
        List<Map> list = new ArrayList<Map>();
        if(StringUtils.equalsIgnoreCase(status, "running")){
            list = jobService.listRunningJob();
        }else if(StringUtils.equalsIgnoreCase(status, "planed")){
            list = jobService.listPlanedJob();
        }
        modelAndView.addObject("list", list);
        modelAndView.addObject("result", 0);
        modelAndView.addObject("msg", "成功");
        return modelAndView;
    }

    @RequestMapping(name = "删除定时任务", value = "/job/delete", method = RequestMethod.GET)
    public Object deleteJob(HttpServletRequest request) throws SchedulerException {
        ModelAndView modelAndView = new ModelAndView();
        RequestUtils requestUtils = new RequestUtils(request);
        String jobName = requestUtils.getParameter("job_name");
        String jobGroup = requestUtils.getParameter("job_group");
        jobService.deleteJob(jobName, jobGroup);
        modelAndView.addObject("result", 0);
        modelAndView.addObject("msg", "成功");
        return modelAndView;
    }

    @RequestMapping(name = "定时任务详情", value = "/job/detail", method = RequestMethod.GET)
    public Object detail(HttpServletRequest request) throws SchedulerException {
        ModelAndView modelAndView = new ModelAndView();
        RequestUtils requestUtils = new RequestUtils(request);
        String jobName = requestUtils.getParameter("job_name");
        String jobGroup = requestUtils.getParameter("job_group");
        Map detail = jobService.detail(jobName, jobGroup);
        modelAndView.addObject("job", detail);
        modelAndView.addObject("result", 0);
        modelAndView.addObject("msg", "成功");
        return modelAndView;
    }


}
