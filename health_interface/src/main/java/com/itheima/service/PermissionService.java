package com.itheima.service;

import com.itheima.entity.PageResult;
import com.itheima.pojo.Permission;

public interface PermissionService {

    public PageResult pageQuery(Integer currentpage, Integer pagesize, String queryString);

    public void add(Permission permission);

    public Permission findPermissionById(Integer id);

    public void edit(Permission permission);

    public void delPermission(Integer id);
}
