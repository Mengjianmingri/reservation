package com.itheima.service;

import com.itheima.pojo.Menu;
import com.itheima.pojo.Permission;

import java.util.List;

public interface MenuService {
    public List<Menu> findMenuAll(String username);
}
