package com.itheima.dao;

import com.github.pagehelper.Page;
import com.itheima.pojo.Setmeal;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface SetmealDao {
    public Page<Setmeal> selectByConditiong(String queryString);
    public void add(Setmeal setmeal);
    public void setSetmelAndCheckGroup(HashMap<String, Integer> map);
    public List<Setmeal> getSetmeal();
    public Setmeal findById(Integer id);
    public List<Map<String, Object>> findSetmelCount();
    public List<Integer> findCheckGroups(Integer id);
    public void edit(Setmeal setmeal);
    public void delCheckGroups(Integer setmealId);
    public void delete(Integer id);
}
