package com.itheima.service;

import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.Role;
import com.itheima.pojo.User;

import java.util.List;

public interface UserService {
     User fingUserByUsername(String username);

    PageResult findPage(QueryPageBean queryPageBean);

    List<Role> findAllRole();

    void add(User user, Integer[] roleIds);

    User finduserByid(Integer id);

    List<Integer> findRoleByUserId(Integer id);

    void edit(User user, Integer[] roleIds);

    void delete(Integer id);

    void UpdatePassword(User user);

    void Updatepassword(User user);
}
