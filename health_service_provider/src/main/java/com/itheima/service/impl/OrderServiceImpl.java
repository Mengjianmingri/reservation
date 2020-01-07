package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.constant.MessageConstant;
import com.itheima.dao.MemberDao;
import com.itheima.dao.OrderDao;
import com.itheima.dao.OrderSettingDao;
import com.itheima.entity.Result;
import com.itheima.pojo.Member;
import com.itheima.pojo.Order;
import com.itheima.pojo.OrderSetting;
import com.itheima.service.OrderService;
import com.itheima.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = OrderService.class)
@Transactional
public class OrderServiceImpl implements OrderService {

    @Autowired
    OrderSettingDao orderSettingDao;
    @Autowired
    MemberDao memberDao;
    @Autowired
    OrderDao orderDao;
    @Override
    public Result order(Map map) throws Exception{
        String orderDate = (String) map.get("orderDate");
        String telephone = (String) map.get("telephone");
        OrderSetting orderSetting = orderSettingDao.findDtaeByOrderDate(DateUtils.parseString2Date(orderDate));
        //检查当前日期是否进行了预约设置
        if(orderSetting==null){
            return new Result(false, MessageConstant.SELECTED_DATE_CANNOT_ORDER);
        }

        //检查预约日期是否预约已满
        if(orderSetting.getReservations()>=orderSetting.getNumber()){
            return new Result(false, MessageConstant.ORDER_FULL);
        }

        //检查用户是否重复预约（同一个用户在同一天预约了同一个套餐），如果是重复预约则无法完成再次预约

        Member member = memberDao.findByTelephone(telephone);
        if(member!=null){
            Integer memberId = member.getId();
            Date order_date = DateUtils.parseString2Date(orderDate);
            String setmealId = (String) map.get("setmealId");
            Order order = new Order(memberId, order_date, Integer.parseInt(setmealId));
            List<Order> orderList = orderDao.findByCondition(order);
            if(orderList!=null&&orderList.size()!=0){
                return new Result(false,MessageConstant.HAS_ORDERED);
            }
        }else {
            //检查当前用户是否为会员，如果是会员则直接完成预约，如果不是会员则自动完成注册并进行预约
            String name = (String) map.get("name");
            String sex = (String) map.get("sex");
            String idCard = (String) map.get("idCard");

            member = new Member();
            member.setName(name);
            member.setSex(sex);
            member.setIdCard(idCard);
            member.setPhoneNumber(telephone);
            member.setRegTime(new Date());
            memberDao.add(member);
        }

        //预约成功，更新当日的已预约人数
        Order order = new Order();
        order.setMemberId(member.getId());
        order.setOrderDate(DateUtils.parseString2Date(orderDate));
        order.setOrderStatus(Order.ORDERSTATUS_NO);
        order.setOrderType((String)map.get("orderType"));
        order.setSetmealId(Integer.parseInt((String)map.get("setmealId")));
        orderDao.add(order);


        orderSetting.setReservations(orderSetting.getReservations()+1);
        orderSettingDao.editReservationsByOrderDate(orderSetting);
        //System.out.println(order.getId());
        return new Result(true,MessageConstant.ORDER_SUCCESS,order.getId());
    }

    @Override
    public Map findById(Integer id) throws Exception{
        Map byId4Detail = orderDao.findById4Detail(id);
        if(byId4Detail!=null){
           Date orderDate = (Date) byId4Detail.get("orderDate");
            byId4Detail.put("orderDate",DateUtils.parseDate2String(orderDate));
        }
        return byId4Detail;
    }
}
