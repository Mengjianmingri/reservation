package com.itheima.dao;

import com.github.pagehelper.Page;
import com.itheima.pojo.CheckItem;

import java.util.List;


public interface CheckItemDao {
    public void add(CheckItem checkItem);
    public Page<CheckItem> selectByConditiong (String queryString);
    public long findCheckItemAndCheckGroupById(Integer id);
    public void delCheckItemById(Integer id);
    public void edit(CheckItem checkItem);
    public CheckItem findByid(Integer id);
    public List<CheckItem> findAll();
}
