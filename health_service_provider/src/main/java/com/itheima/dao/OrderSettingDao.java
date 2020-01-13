package com.itheima.dao;

import com.itheima.pojo.OrderSetting;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface OrderSettingDao {
    public long findCountByOrderDate(Date orderDate);
    public void editNumberByOrderDate(OrderSetting orderSetting);
    public void add(OrderSetting orderSetting);
    public List<Map> getOrderSettingByMonth(Map map);
    public OrderSetting findDtaeByOrderDate(Date parseString2Date);
    public void editReservationsByOrderDate(OrderSetting orderSetting);
}
