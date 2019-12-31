package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.dao.OrderSettingDao;
import com.itheima.pojo.OrderSetting;
import com.itheima.service.OrderSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = OrderSettingService.class)
@Transactional
public class OrderSettingServiceImpl implements OrderSettingService {

    @Autowired
    OrderSettingDao orderSettingDao;

    @Override
    public void add(List<OrderSetting> list) {
        if(list!=null&&list.size()>0){
            for (OrderSetting orderSetting : list) {
                //查数据是否存在
                long count = orderSettingDao.findCountByOrderDate(orderSetting.getOrderDate());
                if(count>0){
                    //存在执行更新操作
                    orderSettingDao.editNumberByOrderDate(orderSetting);
                }else {
                    //不存在执行添加操作
                    orderSettingDao.add(orderSetting);
                }
            }
        }

    }

    @Override
    public List<Map> getOrderSettingByMonth(String date) {

        String begin = date+"-01";
        String end = date+"-31";
        HashMap<String, Object> map = new HashMap<>();
        map.put("begin",begin);
        map.put("end",end);
        List<Map> list = orderSettingDao.getOrderSettingByMonth(map);
        return list;
    }

    @Override
    public void editNumberByDate(OrderSetting orderSetting) {
        long count = orderSettingDao.findCountByOrderDate(orderSetting.getOrderDate());
        if(count>0){
            orderSettingDao.editNumberByOrderDate(orderSetting);
        }else {
            orderSettingDao.add(orderSetting);
        }
    }
}
