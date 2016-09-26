package com.miao.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by 少康 on 2016/6/3.
 */
public class ControllerAspect {
    private static Logger logger = LoggerFactory.getLogger(ControllerAspect.class);

    public void doAfter(JoinPoint jp) {
    }

    public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
        long time = System.currentTimeMillis();
        Object retVal = pjp.proceed();
        time = System.currentTimeMillis() - time;
        String method = pjp.getTarget().getClass().getName() + "." + pjp.getSignature().getName();
        logger.info("\tControllerAspect\t" + method + "\t" + time);
        return retVal;
    }

    public void doBefore(JoinPoint jp) {
    }

    public void doThrowing(JoinPoint jp, Throwable ex) {

    }
}
