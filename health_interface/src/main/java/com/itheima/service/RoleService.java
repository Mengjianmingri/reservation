package com.itheima.service;

import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.Menu;
import com.itheima.pojo.Permission;
import com.itheima.pojo.Role;

import java.util.List;

public interface RoleService {
    public PageResult findRolePage(QueryPageBean queryPageBean);
    public List<Menu> findMenu();
    public List<Permission> findPermission();
    public void addRole(Role role, Integer[] permessionIds,Integer[] menuIds);
    public Role findByRoleId(Integer id);
    public List<Integer> findRoleAndPermessionById(Integer id);
    public List<Integer> findRoleAndMenuById(Integer id);
    public void editRole(Role role, Integer[] permessionIds, Integer[] menuIds);
    public void delRoleById(Integer id);
}
