package com.itheima.dao;

import com.itheima.pojo.Menu;
import com.itheima.pojo.Permission;

import java.util.List;
import java.util.Set;

public interface MenuDao {
    public Set<Integer> findMenuIdByRid(Integer id);
    public Menu findMenuByMid(Integer menuId);
//    public Menu findMenuByParentMenuId(Integer mid);
}
