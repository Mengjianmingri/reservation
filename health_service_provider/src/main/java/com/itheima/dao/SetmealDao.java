package com.itheima.dao;

import com.github.pagehelper.Page;
import com.itheima.pojo.Setmeal;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface SetmealDao {
    Page<Setmeal> selectByConditiong(String queryString);

    void add(Setmeal setmeal);

    void setSetmelAndCheckGroup(HashMap<String, Integer> map);

    List<Setmeal> getSetmeal();

    Setmeal findById(Integer id);

    List<Map<String, Object>> findSetmelCount();
}
