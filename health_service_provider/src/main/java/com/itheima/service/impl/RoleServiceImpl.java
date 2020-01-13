package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.dao.RoleDao;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.Menu;
import com.itheima.pojo.Permission;
import com.itheima.pojo.Role;
import com.itheima.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = RoleService.class)
@Transactional
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleDao roleDao;

    @Override
    public PageResult findRolePage(QueryPageBean queryPageBean) {
        PageHelper.startPage(queryPageBean.getCurrentPage(),queryPageBean.getPageSize());
        Page<Role> page = roleDao.queryByString(queryPageBean.getQueryString());
        return new PageResult(page.getTotal(),page.getResult());
    }

    //添加——回显菜单信息
    @Override
    public List<Menu> findMenu() {
        return roleDao.findMenu();
    }

    //添加——回显权限信息
    @Override
    public List<Permission> findPermission() {
        return roleDao.findPermission();
    }

    //添加角色
    @Override
    public void addRole( Role role,Integer[] permessionIds,Integer[] menuIds) {
        roleDao.add(role);
        setRoleAndPermission(role,permessionIds);
        setRoleAndMenu(role,menuIds);
    }

    //编辑——回显角色信息
    @Override
    public Role findByRoleId(Integer id) {
        return roleDao.findByRoleId(id);
    }

    //编辑——回显权限信息多选框
    @Override
    public List<Integer> findRoleAndPermessionById(Integer id) {
        return roleDao.findRoleAndPermessionById(id);
    }

    //编辑——回显菜单信息多选框
    @Override
    public List<Integer> findRoleAndMenuById(Integer id) {
        return roleDao.findRoleAndMenuById(id);
    }

    //编辑——修改信息
    @Override
    public void editRole(Role role, Integer[] permessionIds, Integer[] menuIds) {
        roleDao.editRole(role);
        roleDao.delRoleAndPermession(role.getId());
        setRoleAndPermission(role,permessionIds);
        roleDao.delRoleAndMenu(role.getId());
        setRoleAndMenu(role,menuIds);
    }


    //删除角色
    @Override
    public void delRoleById(Integer id) {
        long count = roleDao.findUserCountById(id);
        if(count>0){
            throw new RuntimeException("当前角色被用户管理，无法删除");
        }
        roleDao.delRoleAndMenu(id);
        roleDao.delRoleAndPermession(id);
        roleDao.delRoleById(id);
    }


    //添加角色——权限中间表
    private void setRoleAndPermission(Role role, Integer[] permessionIds) {
        Integer RoleId = role.getId();
        if(permessionIds!=null&&permessionIds.length!=0){
            for (Integer permessionId : permessionIds) {
                HashMap<String, Integer> map = new HashMap<>();
                map.put("RoleId",RoleId);
                map.put("PermessionId",permessionId);
                roleDao.setRoleAndPermission(map);
            }
        }
    }

    //添加角色——菜单中间表
    private void setRoleAndMenu(Role role, Integer[] menuIds) {
        Integer RoleId = role.getId();
        if(menuIds!=null&&menuIds.length!=0){
            for (Integer menuId : menuIds) {
                HashMap<String, Integer> map = new HashMap<>();
                map.put("RoleId",RoleId);
                map.put("menuId",menuId);
                roleDao.setRoleAndMenu(map);
            }
        }
    }

}
