package com.itheima.jobs;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.service.PlayInterfaceCountService;

public class InitAllMap {
    @Reference
    private PlayInterfaceCountService playInterfaceCountService;

    public void doInit(){
        playInterfaceCountService.initMapAndDate();
    }
}
