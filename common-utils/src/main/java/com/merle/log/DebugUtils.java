package com.merle.log;

import com.merle.time.TimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

public class DebugUtils {

    /**
     * .
     * 是否输出日志
     */
    public static boolean isOut = true;

    private Logger logger = LoggerFactory.getLogger(DebugUtils.class);

    private String currStr = TimeUtil.lFormat(new Date());

    /**
     * .
     * 类名
     */
    private String clazzName = "";
    /**
     * .
     * 第几次调用log
     */
    private int times = 0;
    /**
     * .
     * 初始化时间
     */
    private long start = System.currentTimeMillis();
    /**
     * .
     * 上次时间
     */
    private long pre = System.currentTimeMillis();

    /**
     * .
     * 附加观赏参数
     *
     * @param key key
     * @param val val
     * @return Debug
     */
    public final DebugUtils appendParam(final String key, final String val) {
        this.clazzName = this.clazzName + "\t" + key + " = " + val;
        return this;
    }

    /**
     * .
     * 构造函数
     *
     * @param clazz 类
     */
    public DebugUtils(final Class<?> clazz) {
        if (clazz != null) {
            this.clazzName = clazz.getSimpleName();
            logger = LoggerFactory.getLogger(clazz);
        }
    }

    /**
     * .
     * 输出观赏日志
     */
    public final void log() {
        if (!isOut) {
            return;
        }
        times++;
        long curr = System.currentTimeMillis();
        String msg = currStr + "位置：" + times + "\t" + this.clazzName + "\t"
                + "费时：" + (curr - start) + "ms\t" + (curr - pre) + "ms";
        //
        logger.info(msg);
        this.pre = curr;
    }

    /**
     * .
     * 输出观赏日志
     *
     * @param identify 标记
     */
    public final void log(final String identify) {
        if (!isOut) {
            return;
        }
        times++;
        long curr = System.currentTimeMillis();
        String msg = currStr + "位置：" + times + "\t" + this.clazzName + "\t"
                + "总计：" + (curr - start) + "ms\t" + identify
                + "花费：" + (curr - pre) + "ms";

        logger.info(msg);
        this.pre = curr;
    }

}
