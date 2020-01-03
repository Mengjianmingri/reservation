package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.dao.SetmealDao;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.Setmeal;
import com.itheima.service.SetmealService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(interfaceClass =SetmealService.class )
@Transactional
public class SetmealServiceImpl implements SetmealService {
    @Autowired
    SetmealDao setmealDao;
    @Autowired
    FreeMarkerConfigurer freeMarkerConfigurer;
    @Value("${out_put_path}")
    String outPutPath;
    @Override
    public PageResult findpage(QueryPageBean queryPageBean) {
        PageHelper.startPage(queryPageBean.getCurrentPage(),queryPageBean.getPageSize());
        Page<Setmeal> page = setmealDao.selectByConditiong(queryPageBean.getQueryString());
        return new PageResult(page.getTotal(),page.getResult());
    }

    @Override
    public void add(Setmeal setmeal, Integer[] checkgroupIds) {
        setmealDao.add(setmeal);
        setSetmealAndCheckGroup(setmeal,checkgroupIds);
        generateMobileStaticHtml();
    }

    //生成静态页面
    public void generateMobileStaticHtml(){
        //准备模板文件
        List<Setmeal> setmealList = setmealDao.getSetmeal();
        //生成套餐列表静态页面
        generateMobileSetmealListHtml(setmealList);
        //生成套餐详情静态页面
        generateMobileSetmealDetailHtml(setmealList);
    }


    //生成套餐列表静态页面
    private void generateMobileSetmealListHtml(List<Setmeal> setmealList) {
        Map map = new HashMap();
        map.put("setmealList",setmealList);
        generateHtml("mobile_setmeal.ftl","m_setmeal.html",map);
    }
    //生成套餐详情静态页面
    private void generateMobileSetmealDetailHtml(List<Setmeal> setmealList) {
        for (Setmeal setmeal : setmealList) {
            Map map = new HashMap();
            map.put("setmeal",setmealDao.findById(setmeal.getId()));
            generateHtml("mobile_setmeal_detail.ftl","setmeal_detail_"+setmeal.getId()+".html",map);
        }

    }

    //生成静态通用页面 模板
    public void generateHtml(String DtName, String JtName, Map map){
        Configuration configuration = freeMarkerConfigurer.getConfiguration();
        try {
            Template template = configuration.getTemplate(DtName);
            Writer out = new FileWriter(new File(outPutPath + JtName));
            template.process(map,out);
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Setmeal> getSetmeal() {
        return setmealDao.getSetmeal();
    }

    @Override
    public Setmeal findById(Integer id) {
        return setmealDao.findById(id);
    }

      public void setSetmealAndCheckGroup(Setmeal setmeal, Integer[] checkgroupIds){
            Integer setmealId = setmeal.getId();
            if(checkgroupIds!=null&&checkgroupIds.length!=0){
                for (Integer checkgroupId : checkgroupIds) {
                    HashMap<String, Integer> map = new HashMap<>();
                    map.put("setmealId",setmealId);
                    map.put("checkgroupId",checkgroupId);
                    setmealDao.setSetmelAndCheckGroup(map);
                }
            }
    }


}
