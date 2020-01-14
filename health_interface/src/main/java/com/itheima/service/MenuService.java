package com.itheima.service;

import com.itheima.entity.PageResult;
import com.itheima.pojo.Menu;
import com.itheima.pojo.Permission;

import java.util.List;

public interface MenuService {
    public List<Menu> findMenuAll(String username);

    public PageResult pageQuery(Integer currentpage, Integer pagesize, String queryString);

    public void add(Menu menu);

    public void delMenuById(Integer id);

    public List<Menu> findAllOneleve();

    public Menu findById(Integer id);

    public void edit(Menu menu);
}
