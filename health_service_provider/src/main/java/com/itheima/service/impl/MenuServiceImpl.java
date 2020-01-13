package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.dao.MenuDao;
import com.itheima.dao.RoleDao;
import com.itheima.dao.UserDao;
import com.itheima.pojo.Menu;
import com.itheima.pojo.Permission;
import com.itheima.pojo.Role;
import com.itheima.pojo.User;
import com.itheima.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service(interfaceClass = MenuService.class)
@Transactional
public class MenuServiceImpl implements MenuService {

    @Autowired
    private MenuDao menuDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private RoleDao roleDao;

    @Override
    public List<Menu> findMenuAll(String username) {
        User user = userDao.fingUserByUsername(username);
        Set<Role> Roles = roleDao.findByUserId(user.getId());

        //根据角色id获取菜单id
        Set<Integer> menuIds = new HashSet<>();
        for (Role role : Roles) {
            menuIds = menuDao.findMenuIdByRid(role.getId());
        }

        //根据菜单id查询菜单
        List<Menu> menus = new ArrayList<>();
        for (Integer menuId : menuIds) {
            Menu menu = menuDao.findMenuByMid(menuId);
            if(menu != null){
                menus.add(menu);
            }
        }
        return menus;
    }
}
