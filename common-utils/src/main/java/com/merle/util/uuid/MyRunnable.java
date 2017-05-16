package com.merle.util.uuid;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

import java.util.List;

/**
 * Created by merle on 2017/5/16.
 */
public class MyRunnable implements Runnable {
    String watchkeys = "watchkeys";// 监视keys
    Jedis jedis = new Jedis("118.192.205.4", 6379);
    @Override
    public void run() {
        try {
            jedis.watch(watchkeys);// watchkeys

            String val = jedis.get(watchkeys);
            int valint = Integer.valueOf(val);
            String userifo = UUIDUtils.base58Uuid();
            if (valint < 10) {
                Transaction tx = jedis.multi();// 开启事务

                tx.incr("watchkeys");

                List<Object> list = tx.exec();// 提交事务，如果此时watchkeys被改动了，则返回null
                if (list != null) {
                    System.out.println( userifo + "success, total:" + valint);
                    /* 抢购成功业务逻辑 */
                    jedis.sadd("setsucc", userifo);
                } else {
                    System.out.println("用户:" + userifo + ":fail");
                    /* 抢购失败业务逻辑 */
                    jedis.sadd("setfail", userifo);
                }

            } else {
                System.out.println("用户:" + userifo + ":fail");
//                System.out.println("用户："+ userifo + ":fail");
                jedis.sadd("setfail", userifo);
                // Thread.sleep(500);
                return;
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            jedis.close();
        }
    }
}
