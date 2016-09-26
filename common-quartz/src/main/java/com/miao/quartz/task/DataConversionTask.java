package com.miao.quartz.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by 少康 on 2016/6/1.
 */
public class DataConversionTask {
    private static final Logger logger = LoggerFactory.getLogger(DataConversionTask.class);
    public void run() {
        logger.info("数据转换任务线程开始执行");
    }
}
