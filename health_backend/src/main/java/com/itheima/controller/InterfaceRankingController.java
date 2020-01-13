package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.entity.Result;
import com.itheima.pojo.MyTuple;
import com.itheima.service.PlayInterfaceCountService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


@RestController
@RequestMapping("/ranking")
public class InterfaceRankingController {
    private static final String RANKING_SUCCESS ="榜单查询成功";
    private static final String RANKING_FAIL ="榜单查询失败";
    @Reference
    private PlayInterfaceCountService playInterfaceCountService;
    /**
     * 实时榜单排行接口实现
     * @return
     */
    @RequestMapping("/getRanking")
    public Result getRanking(){
        try {
            Map<String, Set<MyTuple>> rankingMap = playInterfaceCountService.ranking();
            Map<String, Object> data = new ConcurrentHashMap<>();
            Map<String,List<String>> className = new HashMap<>();
            for (String s : rankingMap.keySet()) {
                List<String> methodName = new ArrayList<>();
                Set<MyTuple> myTuples = rankingMap.get(s);
                for (MyTuple myTuple : myTuples) {
                    String name = myTuple.getName();
                    methodName.add(name);
                }
                className.put(s,methodName);
            }
            data.put("rankingMap",rankingMap);
            data.put("className",className);
            return new Result(true,RANKING_SUCCESS,data);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,RANKING_FAIL);
        }

    }


    @RequestMapping("/getOldDateRanking")
    public Result getOldDateRanking(String date){
        try {
            Map<String, Set<MyTuple>> rankingMap = playInterfaceCountService.oldDateRanking(date);
            Map<String, Object> data = new ConcurrentHashMap<>();
            Map<String,List<String>> className = new HashMap<>();
            for (String s : rankingMap.keySet()) {
                List<String> methodName = new ArrayList<>();
                Set<MyTuple> myTuples = rankingMap.get(s);
                for (MyTuple myTuple : myTuples) {
                    String name = myTuple.getName();
                    methodName.add(name);
                }
                className.put(s,methodName);
            }
            data.put("rankingMap",rankingMap);
            data.put("className",className);
            return new Result(true,RANKING_SUCCESS,data);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,RANKING_FAIL);
        }
    }
}
