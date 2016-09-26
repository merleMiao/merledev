package com.miao.quartz.service;

import org.quartz.SchedulerException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by 少康 on 2016/6/2.
 */
@Service
public interface JobService {

    public void addJob(String jobName, String jobGroup, String jobDesc, Map jobData, String cronExpression, long startTime, long endTime);

    public void pauseJob(String jobName, String jobGroup) throws SchedulerException;

    public void resumeJob(String jobName, String jobGroup) throws SchedulerException;

    public void deleteJob(String jobName, String jobGroup) throws SchedulerException;

    public List<Map> listPlanedJob() throws SchedulerException;

    public List<Map> listRunningJob() throws SchedulerException;

    public Map detail(String jobName, String jobGroup) throws SchedulerException;

}
