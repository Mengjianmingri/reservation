package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.dao.CheckGroupDao;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.CheckGroup;
import com.itheima.service.CheckGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(interfaceClass =CheckGroupService.class )
@Transactional
public class CheckGroupServiceImpl implements CheckGroupService {

    @Autowired
    private CheckGroupDao checkGroupDao;

    @Override
    public PageResult pagequery(QueryPageBean queryPageBean) {
        PageHelper.startPage(queryPageBean.getCurrentPage(),queryPageBean.getPageSize());
        Page<CheckGroup> page = checkGroupDao.selectByConditiong(queryPageBean.getQueryString());
        return new PageResult(page.getTotal(),page.getResult());
    }

    @Override
    public void add(CheckGroup checkGroup, Integer[] checkitemIds) {
        checkGroupDao.add(checkGroup);
        setCheckGroupAndCheckItem(checkGroup, checkitemIds);
    }



    @Override
    public CheckGroup findCheckGroupByid(Integer id) {
        return checkGroupDao.findCheckGroupByid(id);
    }

    @Override
    public List<Integer> findCheckItemByCheckGroupId(Integer id) {
        return checkGroupDao.findCheckItemByCheckGroupId(id);
    }

    @Override
    public void edit(CheckGroup checkGroup, Integer[] checkitemIds) {
        checkGroupDao.delAssociation(checkGroup.getId());
        setCheckGroupAndCheckItem(checkGroup,checkitemIds);
        checkGroupDao.edit(checkGroup);
    }

    @Override
    public List<CheckGroup> findAll() {
        return checkGroupDao.findAll();
    }

    @Override
    public void delete(Integer id) {
        Long count = checkGroupDao.findSetmealCountByCheckGroupId(id);
        if(count>0){
            throw new RuntimeException("删不掉");
        }
        checkGroupDao.deleteCheckGroupAndCheckItemByCheckGroupId(id);
        checkGroupDao.delete(id);
    }

    private void setCheckGroupAndCheckItem(CheckGroup checkGroup, Integer[] checkitemIds) {
        Integer checkGroupId = checkGroup.getId();
        if(checkitemIds!=null&&checkitemIds.length!=0){
            for (Integer checkItemId : checkitemIds) {
                HashMap<String, Integer> map = new HashMap<>();
                map.put("checkGroupId",checkGroupId);
                map.put("checkItemId",checkItemId);
                checkGroupDao.setCheckGroupAndCheckItem(map);
            }
        }
    }
}
