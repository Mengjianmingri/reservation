package com.itheima.service;

import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.Setmeal;

import java.util.List;
import java.util.Map;

public interface SetmealService {
    PageResult findpage(QueryPageBean queryPageBean);

    void add(Setmeal setmeal, Integer[] checkgroupIds);

    List<Setmeal> getSetmeal();

    Setmeal findById(Integer id);

    List<Map<String, Object>> findSetmelCount();

    Setmeal findSetmel(Integer id);

    List<Integer> findCheckGroups(Integer id);

    void edit(Setmeal setmeal, Integer[] checkgroupIds);
}
