package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.entity.Result;
import com.itheima.pojo.Menu;
import com.itheima.pojo.Permission;
import com.itheima.service.MenuService;
import com.itheima.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.List;

@RequestMapping("/menu")
@RestController
public class MenuController {

    @Reference
    private MenuService menuService;
    @Autowired
    private JedisPool jedisPool;

    @Reference
    private UserService userService;

    //登录后动态获取级联菜单
    @RequestMapping("/findMenuAll")
    public Result findMenuAll(String username) {
        List<Menu> menuList = null;
        try {
            List<String> lrange = jedisPool.getResource().lrange(username, 0, -1);
            if (jedisPool.getResource().lrange(username, 0, -1).size()==0) {
                menuList = menuService.findMenuAll(username);
                    jedisPool.getResource().lpush(username, JSON.toJSONString(menuList));

            }else {
                List<String> menus = jedisPool.getResource().lrange(username, 0, -1);
                List<Menu> menu1 = JSON.parseArray(menus.get(0), Menu.class);
                    menuList=menu1;
            }
            return new Result(true, "查询菜单成功", menuList);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "查询菜单失败");
        }
    }

    @RequestMapping("/findpage")
    public PageResult findpage(@RequestBody QueryPageBean queryPageBean){
        PageResult result = menuService.pageQuery(queryPageBean.getCurrentPage(),
                queryPageBean.getPageSize(), queryPageBean.getQueryString());
        return result;
    }

    @RequestMapping("/add")
    public Result add(@RequestBody Menu menu){
        try {
            menuService.add(menu);
            return new Result(true,"添加菜单项成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"添加菜单项失败");
        }
    }

    @RequestMapping("/delMenuById")
    public Result delMenuById(Integer id){
        try {
            menuService.delMenuById(id);
            return new Result(true,"删除菜单项成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"删除菜单项失败");
        }
    }

    @RequestMapping("/findAllOneleve")
    public Result findAllOneleve(){
        try {
            List<Menu> menus = menuService.findAllOneleve();
            return new Result(true,"查询一级菜单项成功",menus);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"查询一级菜单项失败");
        }
    }

    @RequestMapping("/findById")
    public Result findById(Integer id){
        try {
            Menu menu = menuService.findById(id);
            return new Result(true,"根据ID回显数据成功",menu);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"根据ID回显数据失败");
        }
    }

    @RequestMapping("/edit")
    public Result edit(@RequestBody Menu menu){
        try {
            menuService.edit(menu);
            return new Result(true,"修改菜单样式成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"修改菜单项失败");
        }
    }
}
