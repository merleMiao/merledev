package com.miao.quartz.job;

import com.merle.io.JsonUtils;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

/**
 * Created by 少康 on 2016/6/2.
 */
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class CallbackServiceJob implements Job, Serializable {

    private Logger logger = LoggerFactory.getLogger(CallbackServiceJob.class);

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        JobDetail jd = jobExecutionContext.getJobDetail();
        JobDataMap jobDataMap = jd.getJobDataMap();
        logger.info("callback.jobDate:" + JsonUtils.toJson(jobDataMap));
    }
}
