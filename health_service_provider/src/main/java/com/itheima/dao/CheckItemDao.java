package com.itheima.dao;

import com.github.pagehelper.Page;
import com.itheima.pojo.CheckItem;

import java.util.List;


public interface CheckItemDao {
    void add(CheckItem checkItem);

    Page<CheckItem> selectByConditiong (String queryString);

    long findCheckItemAndCheckGroupById(Integer id);

    void delCheckItemById(Integer id);

    void edit(CheckItem checkItem);

    CheckItem findByid(Integer id);

    List<CheckItem> findAll();
}
