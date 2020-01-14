package com.itheima.dao;

import com.github.pagehelper.Page;
import com.itheima.pojo.Menu;
import com.itheima.pojo.Permission;

import java.util.List;
import java.util.Set;

public interface MenuDao {
    public Set<Integer> findMenuIdByRid(Integer id);
    public Menu findMenuByMid(Integer menuId);
//    public Menu findMenuByParentMenuId(Integer mid);

    //查询全部菜单信息
    public Page<Menu> selectByCondition(String queryString);
    //添加菜单项
    public void add(Menu menu);
    //删除依赖
    public void delRoleAndMenuByMenuId(Integer id);
    //删除菜单项
    public void delMenuById(Integer id);
    //查询所有一级菜单
    public List<Menu> findAllOneleve();
    //修改菜单项
    public void updateMenu(Menu menu);
    //根据父级 菜单查询对应的菜单集合
    public List<Menu> findByparentMenuId(Integer parentMenuId);
    //根据菜单ID查询菜单项
    public Menu findMenuByid(Integer id);
}
