package com.itheima.jobs;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.constant.RedisConstant;
import com.itheima.service.OrderSettingService;
import com.itheima.utils.QiniuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * 自定义Job，实现定时清理垃圾预约信息
 */


@Controller
public class ClearOrderSettingJob {
    @Reference
    OrderSettingService orderSettingService;
      //  @Scheduled(cron = "0 0 0 0 10/1 ? ")
    @Scheduled(cron = "0/10 * * * * ?")
    public  void clearOrdersetting() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
        Calendar c = Calendar.getInstance();
        //过去一月
        c.setTime(new Date());
        c.add(Calendar.MONTH, -1);
        Date m = c.getTime();
        String mon = format.format(m);
        List<Integer> ids = orderSettingService.findOrderSettingIdBy2Date(mon);
        if(ids!=null&&ids.size()>0){
            for (int id : ids) {
                orderSettingService.delOrderSettingById(id);
            }
            System.out.println("自定义任务执行，清理预约成功" );
        }else {
            System.out.println("无数据" );
        }

    }

//    public static void main(String[] args) {
//        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
//        Calendar c = Calendar.getInstance();
//        //过去一月
//        c.setTime(new Date());
//        c.add(Calendar.MONTH, -1);
//        Date m = c.getTime();
//        String mon = format.format(m);
//        System.out.println(mon);
//    }
}
