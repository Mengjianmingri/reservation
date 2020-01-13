package com.itheima.dao;

import com.itheima.pojo.OrderSetting;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface OrderSettingDao {
    long findCountByOrderDate(Date orderDate);

    void editNumberByOrderDate(OrderSetting orderSetting);

    void add(OrderSetting orderSetting);

    List<Map> getOrderSettingByMonth(Map map);

    OrderSetting findDtaeByOrderDate(Date parseString2Date);

    void editReservationsByOrderDate(OrderSetting orderSetting);

    List<Integer> findOrderSettingIdBy2Date(Map map);

    void deleteOrderSetting(int id);
}
