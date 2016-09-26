package com.miao.quartz.dao.impl;

import com.merle.base.impl.DaoImpl;
import com.miao.quartz.dao.JobDao;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.stereotype.Repository;

import java.util.*;

/**
 * Created by 少康 on 2016/6/2.
 */
@Repository
public class JobDaoImpl extends DaoImpl implements JobDao {

    private static Scheduler _scheduler = null;

    public static Scheduler getScheduler() {
        if (_scheduler == null) {
            synchronized (JobDaoImpl.class) {
                try {
                    _scheduler = StdSchedulerFactory.getDefaultScheduler();
                } catch (SchedulerException e) {
                    e.printStackTrace();
                }
            }
        }
        return _scheduler;
    }

    @Override
    public void schedule(String jobName, String jobGroup, String jobDesc, JobDataMap data, Class jobClass, String triName, String triGroup, String triDesc, String cronExpression, Date startTime, Date endTime) {
        try {
            Scheduler scheduler = getScheduler();
            JobDetail job = JobBuilder.newJob(jobClass)
                    .withIdentity(jobName, jobGroup)
                    .withDescription(jobDesc)
                    .usingJobData(data)
                    .storeDurably().build();
            CronTrigger trigger = TriggerBuilder.newTrigger()
                    .withIdentity(triName, triGroup)
                    .startAt(startTime)
                    .endAt(endTime)
                    .withSchedule(CronScheduleBuilder.cronSchedule(cronExpression))
                    .withDescription(triDesc)
                    .build();
            scheduler.deleteJob(job.getKey());
            scheduler.scheduleJob(job, trigger);
            scheduler.rescheduleJob(trigger.getKey(), trigger);
        } catch (SchedulerException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void pause(String jobName, String jobGroup) throws SchedulerException {
        Scheduler scheduler = getScheduler();
        JobKey jobKey = new JobKey(jobName, jobGroup);
        scheduler.pauseJob(jobKey);
    }

    @Override
    public void resume(String jobName, String jobGroup) throws SchedulerException {
        Scheduler scheduler = getScheduler();
        JobKey jobKey = new JobKey(jobName, jobGroup);
        scheduler.resumeJob(jobKey);
    }

    @Override
    public void delete(String jobName, String jobGroup) throws SchedulerException {
        Scheduler scheduler = getScheduler();
        JobKey jobKey = new JobKey(jobName, jobGroup);
        scheduler.deleteJob(jobKey);
    }

    @Override
    public List<Map> listRunning() throws SchedulerException {
        Scheduler scheduler = getScheduler();
        List<JobExecutionContext> executingJobs = scheduler.getCurrentlyExecutingJobs();
        List<Map> jobList = new ArrayList<Map>(executingJobs.size());
        for (JobExecutionContext executingJob : executingJobs) {
            Map job = new HashMap();
            JobDetail jobDetail = executingJob.getJobDetail();
            JobKey jobKey = jobDetail.getKey();
            Trigger trigger = executingJob.getTrigger();
            job.put("job_name", jobKey.getName());
            job.put("job_group", jobKey.getGroup());
            job.put("trigger", trigger.getKey());
            Trigger.TriggerState triggerState = scheduler.getTriggerState(trigger.getKey());
            job.put("job_status", triggerState.name());
            if (trigger instanceof CronTrigger) {
                CronTrigger cronTrigger = (CronTrigger) trigger;
                String cronExpression = cronTrigger.getCronExpression();
                job.put("cron_expression", cronExpression);
            }
            jobList.add(job);
        }
        return jobList;
    }

    @Override
    public List<Map> listPlaned() throws SchedulerException {
        Scheduler scheduler = getScheduler();
        GroupMatcher<JobKey> matcher = GroupMatcher.anyJobGroup();
        Set<JobKey> jobKeys = scheduler.getJobKeys(matcher);
        List<Map> jobList = new ArrayList<Map>();
        for (JobKey jobKey : jobKeys) {
            List<? extends Trigger> triggers = scheduler.getTriggersOfJob(jobKey);
            for (Trigger trigger : triggers) {
                Map job = new HashMap();
                job.put("job_name", jobKey.getName());
                job.put("job_group", jobKey.getGroup());
                job.put("trigger", trigger.getKey());
                Trigger.TriggerState triggerState = scheduler.getTriggerState(trigger.getKey());
                job.put("job_status", triggerState.name());
                if (trigger instanceof CronTrigger) {
                    CronTrigger cronTrigger = (CronTrigger) trigger;
                    String cronExpression = cronTrigger.getCronExpression();
                    job.put("cron_expression", cronExpression);
                }
                jobList.add(job);
            }
        }
        return jobList;
    }

    @Override
    public Map detail(String jobName, String jobGroup) throws SchedulerException {
        Scheduler scheduler = getScheduler();
        Map map = new HashMap();
        JobKey jobKey = new JobKey(jobName, jobGroup);
        JobDetail jobDetail = scheduler.getJobDetail(jobKey);
        List<? extends Trigger> triggers = scheduler.getTriggersOfJob(jobKey);
        map.put("job_name", jobName);
        map.put("job_group", jobName);
        map.put("job_data", jobDetail.getJobDataMap());
        map.put("job_desc", jobDetail.getDescription());
        map.put("job_class", jobDetail.getJobClass());
        List<Map> triggerInfos = new ArrayList<Map>();
        map.put("triggers", triggerInfos);
        for(Trigger trigger : triggers){
            Map triggerInfo = new HashMap();
            TriggerKey key = trigger.getKey();
            triggerInfo.put("trigger", key);
            Trigger.TriggerState triggerState = scheduler.getTriggerState(trigger.getKey());
            triggerInfo.put("state", triggerState.name());
            if (trigger instanceof CronTrigger) {
                CronTrigger cronTrigger = (CronTrigger) trigger;
                String cronExpression = cronTrigger.getCronExpression();
                triggerInfo.put("cron_expression", cronExpression);
            }
            triggerInfos.add(triggerInfo);
        }
        return map;
    }
}
