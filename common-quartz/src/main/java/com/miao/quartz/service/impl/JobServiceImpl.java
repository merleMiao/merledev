package com.miao.quartz.service.impl;

import com.merle.exception.MyException;
import com.merle.scheme.ExpScheme;
import com.miao.quartz.dao.JobDao;
import com.miao.quartz.job.CallbackServiceJob;
import com.miao.quartz.service.JobService;
import org.apache.commons.lang.StringUtils;
import org.quartz.JobDataMap;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by 少康 on 2016/6/2.
 */
@Service
public class JobServiceImpl implements JobService{

    public final static String TRI_FIX = "tri:";

    @Autowired
    JobDao jobDao;

    @Override
    public void addJob(String jobName, String jobGroup, String jobDesc, Map jobData, String cronExpression, long startTime, long endTime) {
        if (StringUtils.isBlank(jobName) || StringUtils.isBlank(jobGroup)) {
            throw new MyException(ExpScheme.PARAM_ERR, "job_name, job_group can not null");
        }
        String triGroup = TRI_FIX + StringUtils.defaultString(jobGroup);
        String triName = TRI_FIX + StringUtils.defaultString(jobName);
        String triDesc = "";
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.putAll(jobData);
        jobDao.schedule(jobName, jobGroup, jobDesc, jobDataMap, CallbackServiceJob.class, triName, triGroup, triDesc, cronExpression, new Date(startTime), new Date(endTime));
    }

    @Override
    public void pauseJob(String jobName, String jobGroup) throws SchedulerException {
        jobDao.pause(jobName, jobGroup);
    }

    @Override
    public void resumeJob(String jobName, String jobGroup) throws SchedulerException {
        jobDao.resume(jobName, jobGroup);
    }

    @Override
    public void deleteJob(String jobName, String jobGroup) throws SchedulerException {
        jobDao.delete(jobName, jobGroup);
    }

    @Override
    public List<Map> listPlanedJob() throws SchedulerException {
        return jobDao.listPlaned();
    }

    @Override
    public List<Map> listRunningJob() throws SchedulerException {
        return jobDao.listRunning();
    }

    @Override
    public Map detail(String jobName, String jobGroup) throws SchedulerException {
        return jobDao.detail(jobName, jobGroup);
    }
}
