package com.miao.mongo;

import com.merle.basic.Config;
import com.mongodb.*;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Created by Miaosk
 * @date Created on 2015/6/16.
 */
public class MongoFactory {

    public static Logger logger = LoggerFactory.getLogger(MongoFactory.class);

    private static String[] hosts = new String[]{"127.0.0.1"};

    private static int port = 27017;

    private static MongoClient db = null;

    static {
        try {
            String[] _hosts = Config.getLocalProperties("mongo.host");
            if (_hosts != null && _hosts.length > 0) {
                hosts = _hosts;
            }
            port = Config.getLocalProperty("mongo.port", 27017);
        } catch (Exception ex) {
        }
    }

    public static void init() {
        int poolSize = Config.getLocalProperty("mongo.poolSize", 150);// 连接数量
        int blockSize = Config.getLocalProperty("mongo.blockSize", 150); // 等待队列长度
        try {
            MongoClientOptions mongoClientOptions = MongoClientOptions.builder()
                    .connectionsPerHost(poolSize)
                    .threadsAllowedToBlockForConnectionMultiplier(blockSize)
                    .socketKeepAlive(true)
                    .connectTimeout(5000)
                    .socketTimeout(5000)
//                    .readPreference(ReadPreference.primary())
                    .maxWaitTime(1000*6)
                    .build();
            List<ServerAddress> seeds = new ArrayList<ServerAddress>();
            for(String host : hosts){
                seeds.add(new ServerAddress(host, port));
            }
            db = new MongoClient(seeds, mongoClientOptions);
        } catch (Exception e) {
            logger.error("MongoFactory.err", e);
            e.printStackTrace();
        }
    }

    public static MongoClient getClient(){
        if(db != null){
            return db;
        }
        init();
        return db;
    }

    public static void main(String[] args){
        MongoClient client = MongoFactory.getClient();
        MongoCollection<Document> collection = client.getDatabase("ftask").getCollection("tasks");
//        Document document = new
//        collection.insertOne();
//        collection.find()
    }

}
