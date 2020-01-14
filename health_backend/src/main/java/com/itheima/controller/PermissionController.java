package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.entity.Result;
import com.itheima.pojo.Permission;
import com.itheima.service.PermissionService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/permission")
public class PermissionController {

    @Reference
    private PermissionService permissionService;

    @RequestMapping("/findByPage")
    public PageResult findByPage(@RequestBody QueryPageBean queryPageBean){
        PageResult pageResult = permissionService.pageQuery(
                queryPageBean.getCurrentPage(), queryPageBean.getPageSize(), queryPageBean.getQueryString());
        return pageResult;
    }

    @RequestMapping("/add")
    public Result add(@RequestBody Permission permission){
        try {
            permissionService.add(permission);
            return new Result(true,"添加权限成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"添加权限失败");
        }
    }

    @RequestMapping("/findPermissionById")
    public Result findPermissionById(Integer id){
        try {
            Permission permission = permissionService.findPermissionById(id);
            return new Result(true,"查询权限项成功",permission);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"查询权限项失败");
        }
    }

    @RequestMapping("/edit")
    public Result edit(@RequestBody Permission formData){
        try {
            permissionService.edit(formData);
            return new Result(true,"修改权限项成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"修改权限项失败");
        }
    }

    @RequestMapping("/del")
    public Result del(Integer id){
        try {
            permissionService.delPermission(id);
            return new Result(true,"删除权限项成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"删除权限项失败");
        }
    }
}
