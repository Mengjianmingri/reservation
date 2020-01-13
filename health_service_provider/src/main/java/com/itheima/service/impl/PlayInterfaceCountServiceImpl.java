package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.pojo.MyTuple;
import com.itheima.service.PlayInterfaceCountService;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Tuple;

import javax.annotation.PostConstruct;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service(interfaceClass = PlayInterfaceCountService.class)
public class PlayInterfaceCountServiceImpl implements PlayInterfaceCountService {
    @Autowired
    private JedisPool jedisPool;
    private volatile Map<String, Set<MyTuple>> ConcurrentHashMap;
    private volatile String date;
    private volatile Map<String, Set<MyTuple>> hashMap1;

    //    volatile ConcurrentHashMap<String,ConcurrentHashMap<String, Set<MyTuple>>> oldDateMap;
    public void play(){
        String method = Thread.currentThread().getStackTrace()[2].getMethodName();
        String[] classNameArray = Thread.currentThread().getStackTrace()[2].getClassName().split("\\.");
        String classnameAndDate = classNameArray[classNameArray.length - 1]+"_"+date;
        jedisPool.getResource().zincrby(classnameAndDate,1,method);
    }

    public Map<String, Set<MyTuple>> ranking(){

        Jedis jedis = jedisPool.getResource();
        ConcurrentHashMap<String, Set<MyTuple>> tempMap1 = new ConcurrentHashMap<>();
        for (String key : this.ConcurrentHashMap.keySet()) {
            String newKey = key.split("_")[0];
            Set<Tuple> rank0 = jedis.zrangeWithScores(key, 0, -1);
            HashSet<MyTuple> myTuples = new HashSet<>();
            for (Tuple tuple : rank0) {
                MyTuple myTuple = new MyTuple(tuple);
                myTuples.add(myTuple);
            }
            tempMap1.put(newKey,myTuples);
        }
        return tempMap1;
    }

    @PostConstruct
    public void initMapAndDate(){
        Date date = new Date();
        SimpleDateFormat fm = new SimpleDateFormat("yyyMMdd");
        this.date =  fm.format(date);
        ConcurrentHashMap<String, Set<MyTuple>> ConcurrentHashMap = new ConcurrentHashMap<>(8);
        Set<MyTuple> myTuples = new HashSet<>();
        ConcurrentHashMap.put("CheckItemController"+"_"+this.date,myTuples);
        ConcurrentHashMap.put("CheckGroupController"+"_"+this.date,myTuples);
        ConcurrentHashMap.put("SetmealController"+"_"+this.date,myTuples);
        this.ConcurrentHashMap = ConcurrentHashMap;
//        ConcurrentHashMap<String, ConcurrentHashMap<String, Set<MyTuple>>> oldDateMap = new ConcurrentHashMap<>();
//        this.oldDateMap = oldDateMap;
    }

    public ConcurrentHashMap<String, Set<MyTuple>> oldDateRanking(String formatOldDate){
        Set<MyTuple> mt = new HashSet<>();
        this.hashMap1 = new ConcurrentHashMap<>(16);
        hashMap1.put("CheckItemController"+"_"+formatOldDate,mt);
        hashMap1.put("CheckGroupController"+"_"+formatOldDate,mt);
        hashMap1.put("SetmealController"+"_"+formatOldDate,mt);
        Jedis jedis = jedisPool.getResource();
        ConcurrentHashMap<String, Set<MyTuple>> tempMap1 = new ConcurrentHashMap<>();
        for (String key : hashMap1.keySet()) {
            String newKey = key.split("_")[0];
            Set<Tuple> rank0 = jedis.zrangeWithScores(key, 0, -1);
            HashSet<MyTuple> myTuples = new HashSet<>();
            for (Tuple tuple : rank0) {
                MyTuple myTuple = new MyTuple(tuple);
                myTuples.add(myTuple);
            }
            tempMap1.put(newKey,myTuples);
        }
        return tempMap1;
        }


}
