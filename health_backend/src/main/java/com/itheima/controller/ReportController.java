package com.itheima.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.Result;
import com.itheima.service.MemberService;
import com.itheima.service.SetmealService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/report")
public class ReportController {

    @Reference
    MemberService memberService;
    @Reference
    SetmealService setmealService;

    @RequestMapping("/getMemberReport")
    public Result getMemberReport(){

        try {
            Map<String,Object> map = new HashMap();
            List<String> list = new ArrayList<>();
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.MONTH,-12);
            for (int i = 0; i <12 ; i++) {
                calendar.add(Calendar.MONTH,1);
                list.add(new SimpleDateFormat("yyyy.MM").format(calendar.getTime()));
            }
            List<Integer> memberCount = memberService.findMemberCountByMonth(list);
            map.put("months",list);
            map.put("memberCount",memberCount);
            return new Result(true, MessageConstant.GET_MEMBER_NUMBER_REPORT_SUCCESS,map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_MEMBER_NUMBER_REPORT_FAIL);
        }
    }

    @RequestMapping("/getSetmealReport")
    public Result getSetmealReport(){
        try {
            Map<String,Object> map = new HashMap<>();
            List<Map<String,Object>> list = setmealService.findSetmelCount();
            map.put("setmealCount",list);
            List<String> setmealNames = new ArrayList<>();
            for (Map<String, Object> maps : list) {
                String names = (String) maps.get("name");
                setmealNames.add(names);
            }
            map.put("setmealNames",setmealNames);
            return  new Result(true,MessageConstant.GET_SETMEAL_COUNT_REPORT_SUCCESS,map);
        } catch (Exception e) {
            e.printStackTrace();
            return  new Result(false,MessageConstant.GET_SETMEAL_COUNT_REPORT_FAIL);
        }
    }

}
