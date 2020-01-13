package com.itheima.service;

import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.CheckGroup;

import java.util.List;

public interface CheckGroupService {
    public PageResult pagequery(QueryPageBean queryPageBean);
    public void add(CheckGroup checkGroup, Integer[] checkitemIds);
    public CheckGroup findCheckGroupByid(Integer id);
    public List<Integer> findCheckItemByCheckGroupId(Integer id);
    public void edit(CheckGroup checkGroup, Integer[] checkitemIds);
    public List<CheckGroup> findAll();
    public void delete(Integer id);
}
