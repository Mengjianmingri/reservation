package com.itheima.service;

import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.Setmeal;

public interface SetmealService {
    PageResult findpage(QueryPageBean queryPageBean);

    void add(Setmeal setmeal, Integer[] checkgroupIds);
}
