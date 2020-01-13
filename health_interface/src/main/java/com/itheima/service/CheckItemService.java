package com.itheima.service;

import com.itheima.entity.PageResult;

import com.itheima.pojo.CheckItem;

import java.util.List;

public interface CheckItemService {
    public void add(CheckItem checkItem);
    public PageResult pageQuery(Integer currentPage ,Integer pageSize,String queryString);
    public void delete(Integer id);
    public void edit(CheckItem checkItem);
    public CheckItem findByid(Integer id);
    public List<CheckItem> findAll();
}
