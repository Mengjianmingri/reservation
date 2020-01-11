package com.itheima.controller;


import com.itheima.constant.MessageConstant;
import com.itheima.constant.RedisMessageConstant;
import com.itheima.entity.Result;
import com.itheima.utils.SMSUtils;
import com.itheima.utils.ValidateCodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;
//验证码
@RestController
@RequestMapping("/validateCode")
public class validateCodeController {
    @Autowired
    JedisPool jedisPool;

    @RequestMapping("/send4Order")
    public Result send4Order(String telephone)throws Exception{
        String code4String = ValidateCodeUtils.generateValidateCode4String(4);
      try {

          SMSUtils.sendShortMessage("SMS_181851207",telephone,code4String);

      }catch (Exception e){
          e.printStackTrace();
          return new Result(true,MessageConstant.SEND_VALIDATECODE_FAIL);
      }
        jedisPool.getResource().setex(telephone+ RedisMessageConstant.SENDTYPE_ORDER,6666666,code4String);
        return new Result(true, MessageConstant.SEND_VALIDATECODE_SUCCESS);
    }

    @RequestMapping("/send4Login")
    public Result send4Login(String telephone)throws Exception{
        String code4String = ValidateCodeUtils.generateValidateCode4String(6);
        try {

            SMSUtils.sendShortMessage("SMS_181851207",telephone,code4String);

        }catch (Exception e){
            e.printStackTrace();
            return new Result(true,MessageConstant.SEND_VALIDATECODE_FAIL);
        }
        jedisPool.getResource().setex(telephone+ RedisMessageConstant.SENDTYPE_LOGIN,6666666,code4String);
        return new Result(true, MessageConstant.SEND_VALIDATECODE_SUCCESS);
    }
}
