package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.dao.MenuDao;
import com.itheima.dao.RoleDao;
import com.itheima.dao.UserDao;
import com.itheima.entity.PageResult;
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

    @Override
    public PageResult pageQuery(Integer currentpage, Integer pagesize, String queryString) {
        PageHelper.startPage(currentpage,pagesize);
        Page<Menu> page = menuDao.selectByCondition(queryString);
        return new PageResult(page.getTotal(),page.getResult());
    }

    @Override
    public void delMenuById(Integer id){
        menuDao.delRoleAndMenuByMenuId(id);
        menuDao.delMenuById(id);
    }

    @Override
    public List<Menu> findAllOneleve() {
        return menuDao.findAllOneleve();
    }

    @Override
    public void add(Menu menu) {
        menuDao.add(menu);
        Integer id = menu.getId();
        Integer parentMenuId = menu.getParentMenuId();

        List<Menu> menuList=null;
        if (parentMenuId!=null){
            //根据父级ID获取最后一条数据
            menuList = menuDao.findByparentMenuId(parentMenuId);
        }else {
            menuList = menuDao.findAllOneleve();
        }

        Menu m = new Menu();
        for (int i = 0; i < menuList.size(); i++) {
            m = menuList.get(menuList.size() - 2);
        }
        //获取上一条数据的path
        String path1 = m.getPath();
        String s1 = path1.substring(path1.length()-1, path1.length());

        int i = Integer.parseInt(s1);
        int i1 = i + 1;
        String s = String.valueOf(i1);

        String s2 = path1.substring(0, path1.length() - 1);

        StringBuilder sb = new StringBuilder();
        sb.append(s2);
        sb.append(s);
        String path = sb.toString();

        Menu mm = new Menu();
        mm.setId(id);
        mm.setPriority(i1);
        mm.setPath(path);
        menuDao.updateMenu(mm);
    }

    @Override
    public Menu findById(Integer id) {
        return menuDao.findMenuByid(id);
    }

    @Override
    public void edit(Menu menu) {
        menuDao.updateMenu(menu);
    }
}
