package com.itheima.dao;

import com.github.pagehelper.Page;
import com.itheima.pojo.Role;
import com.itheima.pojo.User;

import java.util.List;
import java.util.Map;

public interface UserDao {
     User fingUserByUsername(String username);

    Page<User> selectByCondition(String queryString);

    List<Role> findAllRole();

    void setUserAndRole(Map<String, Integer> map);

    void add(User user);

    User finduserByid(Integer id);

    List<Integer> findRoleidByUserId(Integer id);

    void delAssociation(Integer id);

    void edit(User user);

    void deletRoleByUserId(Integer id);

    void deleteUserByUserId(Integer id);

    Long findRoleByUserId(Integer id);

    void UpdatePassword(User user);

    void Updatepassword(User user);
}
