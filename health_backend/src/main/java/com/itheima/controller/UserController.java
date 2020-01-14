package com.itheima.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.constant.RedisConstant;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.entity.Result;
import com.itheima.pojo.Role;
import com.itheima.pojo.User;
import com.itheima.service.UserService;
import com.itheima.utils.QiniuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import redis.clients.jedis.JedisPool;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/user")
public class UserController {
    @Reference
    UserService userService;
    @Autowired
    JedisPool jedisPool;
    @RequestMapping("/upload")
    public Result upload(@RequestParam("imgFile")MultipartFile imgFile){
        try {
            //拿到前端传过来的img名字
            String originalFilename = imgFile.getOriginalFilename();
            //以最后一个.进行切割
            int indexOf = originalFilename.lastIndexOf(".");
            //获取后缀名.img
            String substring = originalFilename.substring(indexOf);
            //产生不重复的Id和.img进行拼接
            String fileName = UUID.randomUUID().toString() + substring;
            QiniuUtils.upload2Qiniu(imgFile.getBytes(),fileName);
            jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_RESOURCES,fileName);
            return new Result(true, MessageConstant.PIC_UPLOAD_SUCCESS,fileName);
        } catch (IOException e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.PIC_UPLOAD_FAIL);
        }
    }
    @RequestMapping("/getUsername")
    public Result getUsername(){
        //当Spring security完成认证后，会将当前用户信息保存到框架提供的上下文对象
        org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        //System.out.println(user);
        if(user != null){
            String username = user.getUsername();
            return new Result(true, MessageConstant.GET_USERNAME_SUCCESS,username);
        }
        return new Result(false, MessageConstant.GET_USERNAME_FAIL);
    }

    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){
        PageResult pageResult=userService.findPage(queryPageBean);
        return pageResult;
    }

    @RequestMapping("/findAllRole")
    public Result findAllRole(){
        List<Role>  list =  userService.findAllRole();
        return new Result(true,"查询角色信息成功",list);
    }

    @RequestMapping("/add")
    @PreAuthorize("hasAnyAuthority('USER_ADD')")
    public Result add(@RequestBody User user,Integer[] roleIds){
        try {
            userService.add(user,roleIds);
            String img = user.getImg();
            jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_DB_RESOURCES,img);
            return new Result(true,"添加用户成功");
        } catch (Exception e) {
            e.printStackTrace();
            return  new Result(false,"添加用户失败");
        }
    }

    @RequestMapping("/finduserByid")
    public Result finduserByid(Integer id){
        User user=userService.finduserByid(id);
        return new Result(true,"查找用户成功",user);
    }

    @RequestMapping("/findRoleByUserId")
    public Result findRoleByUserId(Integer id){
        try {
            List<Integer> roles =userService.findRoleByUserId(id);
            return new Result(true,MessageConstant.QUERY_CHECKITEM_SUCCESS,roles);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_CHECKITEM_FAIL);
        }
    }

    @RequestMapping("/edit")
    public Result edit(@RequestBody User user,Integer[] roleIds){
        userService.edit(user,roleIds);
        return new Result(true,"编辑角色成功");
    }

    @RequestMapping("/delete")
    public Result delete(Integer id){
        try {
            userService.delete(id);
            return new Result(true,"删除用户成功");
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,"删除用户失败");
        }
    }


    //验证新密码是否成功
    @RequestMapping("/Updatepassword")
    public ModelAndView Updatepassword(String username, String newpass, String confirmpass) {

        ModelAndView modelAndView = new ModelAndView();
        try {
            boolean flag = newpass.equals(confirmpass);
            if (!flag) {
                modelAndView.setViewName("/pages/xiugaimima.html");
                modelAndView.addObject(new Result(false, "两次密码输入不匹配，请从新输入"));
                return modelAndView;
            }
            com.itheima.pojo.User user = new com.itheima.pojo.User();
            user.setUsername(username);
            user.setPassword(newpass);

            userService.Updatepassword(user);
            modelAndView.setViewName("/login.html");
            return modelAndView;
        } catch (Exception e) {
            e.printStackTrace();
            modelAndView.setViewName("/pages/xiugaimima.html");
            modelAndView.addObject(new Result(false, "修改密码失败"));
            return modelAndView;
        }
    }



}
