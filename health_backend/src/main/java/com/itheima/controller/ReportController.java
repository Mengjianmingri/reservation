package com.itheima.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.Result;
import com.itheima.service.MemberService;
import com.itheima.service.ReportService;
import com.itheima.service.SetmealService;
import com.itheima.utils.DateUtils;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
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
    @PreAuthorize("hasAnyAuthority('REPORT_VIEW')")
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

    @RequestMapping("/findtutu")
    @PreAuthorize("hasAnyAuthority('REPORT_VIEW')")
    public Result findtutu(@RequestBody Date [] dates){
        try {

            //获取要多少个月
            String end_date = DateUtils.parseDate2String(dates[0]);
            String start_date = DateUtils.parseDate2String(dates[1]);
            String[] end_split = end_date.split("-");
            String[] start_split = start_date.split("-");
            //获取年份差值
            int start_end_year = Integer.parseInt(start_split[0])-Integer.parseInt(end_split[0]);
            //获取月份差值
            int start_end_month = start_end_year*12+Integer.parseInt(start_split[1])-Integer.parseInt(end_split[1]);
            System.out.println(start_end_month);

            Map<String,Object> map = new HashMap();
            List<String> list = new ArrayList<>();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(dates[1]);
            calendar.add(Calendar.MONTH,-start_end_month);
            list.add(new SimpleDateFormat("yyyy.MM").format(dates[0]));
            for (int i = 0; i <start_end_month ; i++) {
                calendar.add(Calendar.MONTH,1);
                list.add(new SimpleDateFormat("yyyy.MM").format(calendar.getTime()));
            }
            List<Integer> memberCount = memberService.findMemberCountByMonth(list);
            map.put("months",list);
            map.put("memberCount",memberCount);
            return new Result(true, MessageConstant.GET_MEMBER_NUMBER_REPORT_SUCCESS,map);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @RequestMapping("/getSetmealReport")
    @PreAuthorize("hasAnyAuthority('REPORT_VIEW')")
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
    @PreAuthorize("hasAnyAuthority('REPORT_VIEW')")
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


    @RequestMapping("/exportBusinessReport4PDF")
    public Result exportBusinessReport4PDF(HttpServletRequest request, HttpServletResponse response) {
        try {
            //定义路径
            String jrxml = request.getSession().getServletContext().getRealPath("template/health_business3.jrxml");
            String jasper = request.getSession().getServletContext().getRealPath("template/health_business3.jasper");
            //编译模板
            JasperCompileManager.compileReportToFile(jrxml,jasper);

            Map<String, Object> map = reportService.getBusinessReportData();
            List<Map> hotSetmeal = (List<Map>) map.get("hotSetmeal");
            //填充数据
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasper, map, new JRBeanCollectionDataSource(hotSetmeal));

            ServletOutputStream out = response.getOutputStream(); response.setContentType("application/PDF");
            response.setHeader("content-Disposition", "attachment;filename=report.PDF");
           //输出文件
            JasperExportManager.exportReportToPdfStream(jasperPrint,out);
            out.flush();
            out.close();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_BUSINESS_REPORT_FAIL,null);
        }

    }















    //会员性别占比饼子图
    @RequestMapping("/getSexReport")
    public Result getSexReport(){
        //转为json
        Map<String,Object> data = new HashMap<>();


        try {
            List<Map<String,Object>> memberCount = memberService.findMemberCount();
            data.put("memberCount",memberCount);

            List<String> memberNames = new ArrayList<>();
            for (Map<String, Object> map : memberCount) {
                String name = (String) map.get("name");
                memberNames.add(name);
            }
            data.put("memberNames",memberNames);
            return new Result(true,MessageConstant.GET_MEMBER_NUMBER_REPORT_SUCCESS,data);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.GET_MEMBER_NUMBER_REPORT_FAIL);
        }
    }




    //会员年龄占比饼子图  方法1
    @RequestMapping("/getAgeReport")
    public Result getAgeReport(){
        //转为json
        Map<String,Object> data = new HashMap<>();


        try {
            //传count
            List<Map<String,Object>> memberCount = memberService.findMemberAgeCount();
            data.put("memberCount",memberCount);

            //传name
            List<String> memberNames = new ArrayList<>();
            for (Map<String, Object> map : memberCount) {
                String name = (String) map.get("name");
                memberNames.add(name);
            }
            data.put("memberNames",memberNames);
            return new Result(true,MessageConstant.GET_MEMBER_NUMBER_REPORT_SUCCESS,data);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.GET_MEMBER_NUMBER_REPORT_FAIL);
        }
    }


}
