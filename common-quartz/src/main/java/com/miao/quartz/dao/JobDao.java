package com.miao.quartz.dao;

import com.merle.base.Dao;
import org.quartz.JobDataMap;
import org.quartz.SchedulerException;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by 少康 on 2016/6/2.
 */
@Repository
public interface JobDao extends Dao {

    //增加任务
    public void schedule(String jobName, String jobGroup, String jobDesc, JobDataMap data, Class jobClass, String triName, String triGroup, String triDesc, String cronExpression, Date startTime, Date endTime);

    //暂停任务
    public void pause(String jobName, String jobGroup) throws SchedulerException;

    //重启任务
    public void resume(String jobName, String jobGroup) throws SchedulerException;

    //删除任务
    public void delete(String jobName, String jobGroup) throws SchedulerException;

    //进行中任务列表
    public List<Map> listRunning() throws SchedulerException;

    //计划中的任务
    public List<Map> listPlaned() throws SchedulerException;

    //详情
    public Map detail(String jobName, String jobGroup) throws SchedulerException;
}
