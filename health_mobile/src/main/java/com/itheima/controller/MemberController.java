package com.itheima.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.itheima.constant.MessageConstant;
import com.itheima.constant.RedisMessageConstant;
import com.itheima.entity.Result;
import com.itheima.pojo.Member;
import com.itheima.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping("/member")
public class MemberController {
    @Reference
    MemberService memberService;

    @Autowired
    JedisPool jedisPool;

    @RequestMapping("/login")
    public Result login (HttpServletResponse response, @RequestBody Map map){

        String telephone = (String) map.get("telephone");
        String validateCode = (String) map.get("validateCode");
        //验证码校验
        String redisCode = jedisPool.getResource().get(telephone + RedisMessageConstant.SENDTYPE_LOGIN);
        if(validateCode!=null&&redisCode!=null&&validateCode.equals(redisCode)){

           Member meaber =  memberService.findByTelephone(telephone);
            if(meaber==null){
                meaber.setPhoneNumber(telephone);
                meaber.setRegTime(new Date());
                memberService.add(meaber);
            }

            Cookie cookie = new Cookie("cookie_telephone",telephone);
            cookie.setPath("/");
            cookie.setMaxAge(60*60*24*30);
            response.addCookie(cookie);

            String json = JSON.toJSON(meaber).toString();
            jedisPool.getResource().setex(telephone,6666666,json);
            return new Result(true,MessageConstant.LOGIN_SUCCESS);
        }else {
            return new Result(false, MessageConstant.VALIDATECODE_ERROR);
        }

    }
}
