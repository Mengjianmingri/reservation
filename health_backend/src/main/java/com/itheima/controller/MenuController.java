package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.itheima.entity.Result;
import com.itheima.pojo.Menu;
import com.itheima.pojo.Permission;
import com.itheima.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
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
}
