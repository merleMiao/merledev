package com.merle.util.uuid;

import redis.clients.jedis.Jedis;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by merle on 2017/5/16.
 */
public class RedisTest {
    public static void main(String[] args) {
        final String watchkeys = "watchkeys";
        ExecutorService executor = Executors.newFixedThreadPool(20);

        final Jedis jedis = new Jedis("118.192.205.4", 6379);
        jedis.set(watchkeys, "0");// 重置watchkeys为0
        jedis.del("setsucc", "setfail");// 清空抢成功的，与没有成功的
        jedis.close();

        for (int i = 0; i < 10000; i++) {// 测试一万人同时访问
            executor.execute(new MyRunnable());
        }
        executor.shutdown();
    }
}
