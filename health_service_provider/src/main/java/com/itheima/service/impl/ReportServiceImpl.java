package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.dao.MemberDao;
import com.itheima.dao.OrderDao;
import com.itheima.service.ReportService;
import com.itheima.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = ReportService.class)
@Transactional
public class ReportServiceImpl implements ReportService {
    @Autowired
    MemberDao memberDao;
    @Autowired
    OrderDao orderDao;

    @Override
    public Map<String, Object> getBusinessReportData()throws Exception {
        Map<String, Object> map = new HashMap<>();
        //本日期
        String today = DateUtils.parseDate2String(DateUtils.getToday());
        //本周一
        String week = DateUtils.parseDate2String(DateUtils.getThisWeekMonday());
       //本月一
        String month = DateUtils.parseDate2String(DateUtils.getFirstDay4ThisMonth());

        //获取今日新增会员
        Integer todayNewMember = memberDao.findMemberCountByDate(today);
        //获取总会员
        Integer totalMember = memberDao.findMemberTotalCount();
        //本周新增会员数
        Integer thisWeekNewMember = memberDao.findMemberCountAfterDate(week);
        //本月新增会员数
        Integer thisMonthNewMember = memberDao.findMemberCountAfterDate(month);
        //今日预约数
        Integer todayOrderNumber = orderDao.findOrderCountByDate(today);
        //今日到诊数
        Integer todayVisitsNumber = orderDao.findVisitsCountByDate(today);
        //本周预约数
        Integer thisWeekOrderNumber = orderDao.findOrderCountAfterDate(week);
        //本周到诊数
        Integer thisWeekVisitsNumber = orderDao.findVisitsCountAfterDate(week);
        //本月预约数
        Integer thisMonthOrderNumber = orderDao.findOrderCountAfterDate(month);
        //本月到诊数
        Integer thisMonthVisitsNumber = orderDao.findVisitsCountAfterDate(month);
        //热门套餐
        List<Map> hotSetmeal = orderDao.findHotSetmeal();

        map.put("reportDate",today);
        map.put("todayNewMember",todayNewMember);
        map.put("totalMember",totalMember);
        map.put("thisWeekNewMember",thisWeekNewMember);
        map.put("thisMonthNewMember",thisMonthNewMember);
        map.put("todayOrderNumber",todayOrderNumber);
        map.put("todayVisitsNumber",todayVisitsNumber);
        map.put("thisWeekOrderNumber",thisWeekOrderNumber);
        map.put("thisWeekVisitsNumber",thisWeekVisitsNumber);
        map.put("thisMonthOrderNumber",thisMonthOrderNumber);
        map.put("thisMonthVisitsNumber",thisMonthVisitsNumber);
        map.put("hotSetmeal",hotSetmeal);
        return map;
    }
}
