package com.itheima.service;

import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.Setmeal;

import java.util.List;
import java.util.Map;

public interface SetmealService {
    public PageResult findpage(QueryPageBean queryPageBean);
    public void add(Setmeal setmeal, Integer[] checkgroupIds);
    public List<Setmeal> getSetmeal();
    public Setmeal findById(Integer id);
    public List<Map<String, Object>> findSetmelCount();
    public Setmeal findSetmel(Integer id);
    public List<Integer> findCheckGroups(Integer id);
    public void edit(Setmeal setmeal, Integer[] checkgroupIds);
    public void delete(Integer id);
}
