package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.dao.PermissionDao;
import com.itheima.dao.RoleDao;
import com.itheima.dao.UserDao;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.Permission;
import com.itheima.pojo.Role;
import com.itheima.pojo.User;
import com.itheima.service.UserService;
import com.itheima.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service(interfaceClass = UserService.class)
@Transactional
public class UserServiceImpl implements UserService {
    @Autowired
    UserDao userDao;
    @Autowired
    RoleDao roleDao;
    @Autowired
    PermissionDao permissionDao;
    public User fingUserByUsername(String username) {

        User user = userDao.fingUserByUsername(username);
        if(user==null){
            return null;
        }
        Integer userId = user.getId();
        Set<Role> roles =  roleDao.findByUserId(userId);
        for (Role role : roles) {
            Integer roleId = role.getId();
            Set<Permission> permissions =  permissionDao.findByRoleId(roleId);
            role.setPermissions(permissions);
        }
        user.setRoles(roles);
        return user;
    }

    @Override
    public PageResult findPage(QueryPageBean queryPageBean) {
        try {
            Integer currentPage = queryPageBean.getCurrentPage();
            String queryString = queryPageBean.getQueryString();
            Integer pageSize = queryPageBean.getPageSize();
            PageHelper.startPage(currentPage,pageSize);
            Page<User> page =userDao.selectByCondition(queryString);
            for (User user : page) {
                Date birthday =user.getBirthday();
                String s = DateUtils.parseDate2String(birthday);
                user.setBirthdaystr(s);
            }
            return new PageResult(page.getTotal(),page.getResult());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    @Override
    public List<Role> findAllRole() {
        return  userDao.findAllRole();
    }

    @Override
    public void add(User user, Integer[] roleIds) {
        String old_password = user.getPassword();
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String password = encoder.encode(old_password);
        user.setPassword(password);
        userDao.add(user);
        if(roleIds != null && roleIds.length > 0){
            setUserAndRole(user,roleIds);
        }
    }


    public User finduserByid(Integer id) {
        User user=userDao.finduserByid(id);
        return user;
    }

    @Override
    public List<Integer> findRoleByUserId(Integer id) {
        return  userDao.findRoleidByUserId(id);
    }

    @Override
    public void edit(User user, Integer[] roleIds) {
        userDao.delAssociation(user.getId());
        setUserAndRole(user,roleIds);
        userDao.edit(user);
    }

    @Override
    public void delete(Integer id) {
        Long count = userDao.findRoleByUserId(id);
        if(count>0){
            userDao.deletRoleByUserId(id);
            userDao.deleteUserByUserId(id);
        }else {
            userDao.deleteUserByUserId(id);
        }
    }

    @Override
    public void UpdatePassword(User user) {

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String newPassword = passwordEncoder.encode(user.getPassword());

        user.setPassword(newPassword);
        userDao.UpdatePassword(user);
    }

    //修改密码
    @Override
    public void Updatepassword(User user) {

        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String encode = bCryptPasswordEncoder.encode(user.getPassword());
        user.setPassword(encode);

        userDao.Updatepassword(user);

    }



    private void setUserAndRole(User user, Integer[] roleIds) {
        Integer userId = user.getId();
        if(roleIds!=null&&roleIds.length!=0){
            for (Integer roleId : roleIds) {
                HashMap<String, Integer> map = new HashMap<>();
                map.put("user_id",userId);
                map.put("role_id",roleId);
                userDao.setUserAndRole(map);
            }
        }
    }

}
