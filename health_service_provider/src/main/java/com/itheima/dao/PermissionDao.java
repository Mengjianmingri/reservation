package com.itheima.dao;

import com.github.pagehelper.Page;
import com.itheima.pojo.Permission;

import java.util.Set;

public interface PermissionDao {
    public Set<Permission> findByRoleId(Integer roleId);

    public Page<Permission> selectByCondition(String queryString);

    public void add(Permission permission);

    public Permission findPermissionById(Integer id);

    public void edit(Permission permission);

    public void deleteCondition(Integer id);

    public void delPermission(Integer id);
}
