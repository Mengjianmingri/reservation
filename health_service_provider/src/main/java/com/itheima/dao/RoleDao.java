package com.itheima.dao;

import com.github.pagehelper.Page;
import com.itheima.pojo.Menu;
import com.itheima.pojo.Permission;
import com.itheima.pojo.Role;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

public interface RoleDao {
    public Set<Role> findByUserId(Integer userId);
    public Page<Role> queryByString(String queryString);
    public List<Menu> findMenu();
    public List<Permission> findPermission();
    public void add(Role role);
    public void setRoleAndPermission(HashMap<String, Integer> map);
    public Role findByRoleId(Integer id);
    public List<Integer> findRoleAndPermessionById(Integer id);
    public List<Integer> findRoleAndMenuById(Integer id);
    public void setRoleAndMenu(HashMap<String, Integer> map);
    public void editRole(Role role);
    public void delRoleAndPermession(Integer id);
    public void delRoleAndMenu(Integer id);
    public void delRoleById(Integer id);
    public long findUserCountById(Integer id);
}
