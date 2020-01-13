package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.entity.Result;
import com.itheima.pojo.Menu;
import com.itheima.pojo.Permission;
import com.itheima.pojo.Role;
import com.itheima.service.RoleService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/role")
@RestController
public class RoleController {

    @Reference
    private RoleService roleService;

    //分页查询
    @RequestMapping("/findRolePage")
    public PageResult findRolePage(@RequestBody QueryPageBean queryPageBean){
        return roleService.findRolePage(queryPageBean);
    }

    //添加——回显菜单信息
    @RequestMapping("/findMenu")
    public Result findMenu(){
        try {
            List<Menu> menuAll = roleService.findMenu();
            return new Result(true,"回显菜单信息成功",menuAll);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"回显菜单信息失败");
        }
    }

    //添加——回显权限信息
    @RequestMapping("/findPermission")
    public Result findPermission(){
        try {
            List<Permission> permissionAll = roleService.findPermission();
            return new Result(true,"回显权限信息成功",permissionAll);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"回显权限信息失败");
        }
    }

    //添加角色
    @RequestMapping("/addRole")
    public Result addRole(@RequestBody Role role,Integer[] permessionIds,Integer[] menuIds){
        try {
            roleService.addRole(role,permessionIds,menuIds);
            return new Result(true,"添加角色信息成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"添加角色信息失败");
        }
    }

    //编辑——回显角色信息
    @RequestMapping("/findByRoleId")
    public Result findByRoleId(Integer id){
        try {
            Role role  = roleService.findByRoleId(id);
            return new Result(true,"查询角色信息成功",role);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"查询角色信息失败");
        }
    }

    //编辑——回显权限信息多选框
    @RequestMapping("/findRoleAndPermessionById")
    public Result findRoleAndPermessionById(Integer id){
        try {
            List<Integer> permessionIds = roleService.findRoleAndPermessionById(id);
            return new Result(true,"查询角色信息成功",permessionIds);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"查询角色信息失败");
        }
    }


    //编辑——回显菜单信息多选框
    @RequestMapping("/findRoleAndMenuById")
    public Result findRoleAndMenuById(Integer id){
        try {
            List<Integer> menuIds = roleService.findRoleAndMenuById(id);
            return new Result(true,"查询角色信息成功",menuIds);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"查询角色信息失败");
        }
    }

    //编辑——修改信息
    @RequestMapping("/editRole")
    public Result editRole(@RequestBody Role role,Integer[] permessionIds,Integer[] menuIds){
        try {
            roleService.editRole(role,permessionIds,menuIds);
            return new Result(true,"编辑角色信息成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"编辑角色信息失败");
        }
    }

    //删除角色
    @RequestMapping("/delRoleById")
    public Result delRoleById(Integer id){
        try {
            roleService.delRoleById(id);
            return new Result(true,"删除角色信息成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"删除角色信息失败");
        }
    }
}
