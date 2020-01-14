package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.service.InitAllMapService;
import com.itheima.service.PlayInterfaceCountService;
import org.springframework.beans.factory.annotation.Autowired;

@Service(interfaceClass = InitAllMapService.class)
public class InitAllMapServiceImpl implements InitAllMapService {
    @Autowired
    private PlayInterfaceCountService playInterfaceCountService;

    public void doInit(){
        playInterfaceCountService.initMapAndDate();
    }
}
