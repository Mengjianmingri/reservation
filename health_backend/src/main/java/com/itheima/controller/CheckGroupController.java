package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.entity.Result;
import com.itheima.pojo.CheckGroup;
import com.itheima.service.CheckGroupService;
import com.itheima.service.PlayInterfaceCountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/checkgroup")
public class CheckGroupController {

    @Reference
    CheckGroupService checkGroupService;

    @Autowired
    private PlayInterfaceCountService playInterfaceCountService;

    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){
        playInterfaceCountService.play();
      return checkGroupService.pagequery(queryPageBean) ;
    }


    @RequestMapping("/add")
    @PreAuthorize("hasAnyAuthority('CHECKGROUP_ADD')")
    public Result add(@RequestBody CheckGroup checkGroup , Integer[] checkitemIds){
        playInterfaceCountService.play();
       try {
           checkGroupService.add(checkGroup,checkitemIds);
           return new Result(true,MessageConstant.ADD_CHECKGROUP_SUCCESS);
       }catch (Exception e){
           e.printStackTrace();
           return new Result(false,MessageConstant.ADD_CHECKGROUP_FAIL);
       }
    }

    @RequestMapping("/findCheckGroupByid")
    @PreAuthorize("hasAnyAuthority('CHECKGROUP_QUERY')")
    public Result findCheckGroupByid(Integer id){
        playInterfaceCountService.play();
        try {
            CheckGroup checkGroup = checkGroupService.findCheckGroupByid(id);
            return new Result(true,MessageConstant.QUERY_CHECKGROUP_SUCCESS,checkGroup);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_CHECKGROUP_FAIL);
        }
    }

    @RequestMapping("/findCheckItemByCheckGroupId")
    public Result findCheckItemByCheckGroupId(Integer id){
        playInterfaceCountService.play();
        try {
            List<Integer> checkitemIds =checkGroupService.findCheckItemByCheckGroupId(id);
            return new Result(true,MessageConstant.QUERY_CHECKITEM_SUCCESS,checkitemIds);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_CHECKITEM_FAIL);
        }
    }

    @RequestMapping("/edit")
    @PreAuthorize("hasAnyAuthority('CHECKGROUP_EDIT')")
    public Result edit(@RequestBody CheckGroup checkGroup , Integer[] checkitemIds){
        playInterfaceCountService.play();
        try {
            checkGroupService.edit(checkGroup,checkitemIds);
            return new Result(true,MessageConstant.ADD_CHECKGROUP_SUCCESS);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,MessageConstant.ADD_CHECKGROUP_FAIL);
        }
    }

    @RequestMapping("/findAll")
    public Result findAll(){
        playInterfaceCountService.play();
        try {
            List<CheckGroup> checkGroupList = checkGroupService.findAll();
            return new Result(true,MessageConstant.QUERY_CHECKGROUP_SUCCESS,checkGroupList);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_CHECKGROUP_FAIL);
        }
    }

    @RequestMapping("/delete")
    @PreAuthorize("hasAnyAuthority('CHECKGROUP_DELETE')")
    public Result delete(Integer id){
        playInterfaceCountService.play();
        try {
           checkGroupService.delete(id);
            return new Result(true,"删除检查组成功");
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,"删除检查组失败");
        }
    }


}
