package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.entity.Result;
import com.itheima.pojo.CheckItem;
import com.itheima.service.CheckItemService;
import com.itheima.service.PlayInterfaceCountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/checkitem")
public class CheckItemController {

    @Reference
     CheckItemService checkItemService;
    @Autowired
    private PlayInterfaceCountService playInterfaceCountService;
    @RequestMapping("/add")
    @PreAuthorize("hasAnyAuthority('CHECKITEM_ADD')")
    public Result add(@RequestBody CheckItem checkItem){
        playInterfaceCountService.play();
        try {
            checkItemService.add(checkItem);
            return new Result(true, MessageConstant.ADD_CHECKITEM_SUCCESS);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false, MessageConstant.ADD_CHECKITEM_FAIL);
        }
    }

    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){
        playInterfaceCountService.play();
      PageResult pageResult =  checkItemService.pageQuery(
              queryPageBean.getCurrentPage(),
              queryPageBean.getPageSize(),
              queryPageBean.getQueryString()
      );
      return pageResult;
    }

    @RequestMapping("/delete")
    @PreAuthorize("hasAnyAuthority('CHECKITEM_DELETE')")
    public Result delete(Integer id){
        playInterfaceCountService.play();
        try {
            checkItemService.delete(id);
            return new Result(true, MessageConstant.DELETE_CHECKITEM_SUCCESS);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false, MessageConstant.DELETE_CHECKITEM_FAIL);
        }
    }
    @RequestMapping("/edit")
    @PreAuthorize("hasAnyAuthority('CHECKITEM_EDIT')")
    public Result edit(@RequestBody CheckItem checkItem){
        playInterfaceCountService.play();
        try {
            checkItemService.edit(checkItem);
            return new Result(true, MessageConstant.EDIT_CHECKITEM_SUCCESS);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false, MessageConstant.EDIT_CHECKITEM_FAIL);
        }
    }

    @RequestMapping("/findByid")
    @PreAuthorize("hasAnyAuthority('CHECKITEM_QUERY')")
    public Result findByid(Integer id){
        playInterfaceCountService.play();
        try {
            CheckItem checkItem = checkItemService.findByid(id);
            return new Result(true, MessageConstant.EDIT_CHECKITEM_SUCCESS,checkItem);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false, MessageConstant.EDIT_CHECKITEM_FAIL);
        }
    }


    @RequestMapping("/findAll")
    public Result findAll(){
        playInterfaceCountService.play();
        try {
            List<CheckItem> checkItemList =  checkItemService.findAll();
            return new Result(true,MessageConstant.QUERY_CHECKITEM_SUCCESS,checkItemList);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_CHECKITEM_FAIL);
        }
    }
}
