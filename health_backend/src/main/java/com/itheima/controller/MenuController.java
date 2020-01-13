package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.entity.Result;
import com.itheima.pojo.Menu;
import com.itheima.pojo.Permission;
import com.itheima.service.MenuService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/menu")
@RestController
public class MenuController {

    @Reference
    private MenuService menuService;

    //登录后动态获取级联菜单
    @RequestMapping("/findMenuAll")
    public Result findMenuAll(String username){
        try {
            List<Menu> menuList = menuService.findMenuAll(username);
            return new Result(true,"查询菜单成功",menuList);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"查询菜单失败");
        }
    }
}
