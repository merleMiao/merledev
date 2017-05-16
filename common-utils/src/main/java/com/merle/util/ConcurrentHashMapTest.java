package com.merle.util;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by merle on 2017/5/16.
 */
public class ConcurrentHashMapTest {
    private static ConcurrentHashMap<Integer, Integer> map = new ConcurrentHashMap<>();
    public static void main(String[] args) throws InterruptedException {
        new Thread("Thread1"){
            @Override
            public void run() {
                map.put(3, 33);
            }
        };


        new Thread("Thread2"){
            @Override
            public void run() {
                map.put(4, 44);
            }
        };

        new Thread("Thread3"){
            @Override
            public void run() {
                map.put(7, 77);
            }
        };
        Thread.sleep(5000l);
        System.out.println(map);
    }
}
