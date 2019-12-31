package com.itheima.dao;

import com.github.pagehelper.Page;
import com.itheima.pojo.Setmeal;

import java.util.HashMap;

public interface SetmealDao {
    Page<Setmeal> selectByConditiong(String queryString);

    void add(Setmeal setmeal);

    void setSetmelAndCheckGroup(HashMap<String, Integer> map);
}
