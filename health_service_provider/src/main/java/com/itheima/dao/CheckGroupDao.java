package com.itheima.dao;

import com.github.pagehelper.Page;
import com.itheima.pojo.CheckGroup;

import java.util.HashMap;
import java.util.List;

public interface CheckGroupDao {

    Page<CheckGroup> selectByConditiong(String queryString);

    void add(CheckGroup checkGroup);

    void setCheckGroupAndCheckItem(HashMap<String, Integer> map);

    CheckGroup findCheckGroupByid(Integer id);

    List<Integer> findCheckItemByCheckGroupId(Integer id);

    void delAssociation(Integer id);

    void edit(CheckGroup checkGroup);

    List<CheckGroup> findAll();
}
