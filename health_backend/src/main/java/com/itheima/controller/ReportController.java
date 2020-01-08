package com.itheima.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.Result;
import com.itheima.service.MemberService;
import com.itheima.service.ReportService;
import com.itheima.service.SetmealService;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/report")
public class ReportController {

    @Reference
    MemberService memberService;
    @Reference
    SetmealService setmealService;
    @Reference
    ReportService reportService;

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

    @RequestMapping("/getBusinessReportData")
    public Result getBusinessReportData(){
        try {
            Map<String,Object> result = reportService.getBusinessReportData();
            return new Result(true,MessageConstant.GET_BUSINESS_REPORT_SUCCESS,result);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.GET_BUSINESS_REPORT_FAIL);
        }
    }


    @RequestMapping("/exportBusinessReport")
    public Result exportBusinessReport(HttpServletRequest request, HttpServletResponse response) {
        try {
            String template = request.getSession().getServletContext().getRealPath("template");
            //File.separator分隔符 适用于Linux \\\\和windows //////
            XSSFWorkbook sheets = new XSSFWorkbook(new FileInputStream(template+ File.separator +"report_template.xlsx"));
            XSSFSheet sheetAt = sheets.getSheetAt(0);

            Map<String, Object> map = reportService.getBusinessReportData();
            String reportDate = (String) map.get("reportDate");
            sheetAt.getRow(2).getCell(5).setCellValue(reportDate);

            Integer todayNewMember = (Integer) map.get("todayNewMember");
            sheetAt.getRow(4).getCell(5).setCellValue(todayNewMember);

            Integer totalMember = (Integer) map.get("totalMember");
            sheetAt.getRow(4).getCell(7).setCellValue(totalMember);

            Integer thisWeekNewMember = (Integer) map.get("thisWeekNewMember");
            sheetAt.getRow(5).getCell(5).setCellValue(thisWeekNewMember);

            Integer thisMonthNewMember = (Integer) map.get("thisMonthNewMember");
            sheetAt.getRow(5).getCell(7).setCellValue(thisMonthNewMember);

            Integer todayOrderNumber = (Integer) map.get("todayOrderNumber");
            sheetAt.getRow(7).getCell(5).setCellValue(todayOrderNumber);

            Integer todayVisitsNumber = (Integer) map.get("todayVisitsNumber");
            sheetAt.getRow(7).getCell(7).setCellValue(todayVisitsNumber);

            Integer thisWeekOrderNumber = (Integer) map.get("thisWeekOrderNumber");
            sheetAt.getRow(8).getCell(5).setCellValue(thisWeekOrderNumber);

            Integer thisWeekVisitsNumber = (Integer) map.get("thisWeekVisitsNumber");
            sheetAt.getRow(8).getCell(7).setCellValue(thisWeekVisitsNumber);

            Integer thisMonthOrderNumber = (Integer) map.get("thisMonthOrderNumber");
            sheetAt.getRow(9).getCell(5).setCellValue(thisMonthOrderNumber);

            Integer thisMonthVisitsNumber = (Integer) map.get("thisMonthVisitsNumber");
            sheetAt.getRow(9).getCell(7).setCellValue(thisMonthVisitsNumber);

            List<Map> hotSetmeal = (List<Map>) map.get("hotSetmeal");
            int rownum=12;
            for (Map map1 : hotSetmeal) {
                String name = (String) map1.get("name");
                Long setmeal_count = (Long) map1.get("setmeal_count");
                BigDecimal proportion = (BigDecimal) map1.get("proportion");
                sheetAt.getRow(rownum).getCell(4).setCellValue(name);
                sheetAt.getRow(rownum).getCell(5).setCellValue(setmeal_count);
                sheetAt.getRow(rownum).getCell(6).setCellValue(proportion.doubleValue());
                rownum++;
            }
            ServletOutputStream out = response.getOutputStream(); response.setContentType("application/vnd.ms-excel");
            response.setHeader("content-Disposition", "attachment;filename=report.xlsx");
            sheets.write(out);
            out.flush();
            out.close();
            sheets.close();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_BUSINESS_REPORT_FAIL,null);
        }

    }


}
