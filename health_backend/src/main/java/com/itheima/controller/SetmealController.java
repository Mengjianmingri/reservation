package com.itheima.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.constant.RedisConstant;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.entity.Result;
import com.itheima.pojo.Setmeal;
import com.itheima.service.SetmealService;
import com.itheima.utils.QiniuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import redis.clients.jedis.JedisPool;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/setmeal")
public class SetmealController {

    @Reference
    SetmealService setmealService;

   /* @Autowired
    JedisPool jedisPool;
*/
   /* @RequestMapping("/upload")
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
            jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_RESOURCES);
            return new Result(true, MessageConstant.PIC_UPLOAD_SUCCESS,fileName);
        } catch (IOException e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.PIC_UPLOAD_FAIL);
        }

    }*/


    @RequestMapping("/findPage")
    public PageResult findPage (@RequestBody QueryPageBean queryPageBean){
        return setmealService.findpage(queryPageBean);
    }

   /* @RequestMapping("/add")
    public Result add(@RequestBody Setmeal setmeal , Integer[] checkgroupIds){
        try {
            setmealService.add(setmeal,checkgroupIds);
            jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_DB_RESOURCES);
            return new Result(true,MessageConstant.ADD_SETMEAL_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(true,MessageConstant.ADD_SETMEAL_FAIL);
        }

    }*/

    @RequestMapping("/add1")
    public Result add1( Setmeal setmeal , Integer[] checkgroupIds,@RequestParam("file") MultipartFile imgFile){
        try {
            setmealService.add(setmeal,checkgroupIds);
               //拿到前端传过来的img名字
            String originalFilename = imgFile.getOriginalFilename();
            //以最后一个.进行切割
            int indexOf = originalFilename.lastIndexOf(".");
            //获取后缀名.img
            String substring = originalFilename.substring(indexOf);
            //产生不重复的Id和.img进行拼接
            String fileName = UUID.randomUUID().toString() + substring;
            QiniuUtils.upload2Qiniu(imgFile.getBytes(),fileName);
            return new Result(true,MessageConstant.ADD_SETMEAL_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.ADD_SETMEAL_FAIL);
        }

    }



}
