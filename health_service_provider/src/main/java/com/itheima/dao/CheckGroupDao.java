package com.itheima.dao;

import com.github.pagehelper.Page;
import com.itheima.pojo.CheckGroup;

import java.util.HashMap;
import java.util.List;

public interface CheckGroupDao {
    public Page<CheckGroup> selectByConditiong(String queryString);
    public void add(CheckGroup checkGroup);
    public void setCheckGroupAndCheckItem(HashMap<String, Integer> map);
    public CheckGroup findCheckGroupByid(Integer id);
    public List<Integer> findCheckItemByCheckGroupId(Integer id);
    public void delAssociation(Integer id);
    public void edit(CheckGroup checkGroup);
    public List<CheckGroup> findAll();
    public Long findSetmealCountByCheckGroupId(Integer id);
    public void deleteCheckGroupAndCheckItemByCheckGroupId(Integer id);
    public void delete(Integer id);
}
