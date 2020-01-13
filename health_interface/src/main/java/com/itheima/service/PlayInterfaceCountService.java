package com.itheima.service;

import com.itheima.pojo.MyTuple;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public interface PlayInterfaceCountService {
    public void play();
    public Map<String, Set<MyTuple>> ranking();
    public void initMapAndDate();
    public Map<String, Set<MyTuple>> oldDateRanking(String formatOldDate);
}
