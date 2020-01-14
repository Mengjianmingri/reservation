package com.itheima.dao;

import com.github.pagehelper.Page;
import com.itheima.pojo.Member;

import java.util.List;
import java.util.Map;

public interface MemberDao {
    public List<Member> findAll();
    public Page<Member> selectByCondition(String queryString);
    public void add(Member member);
    public void deleteById(Integer id);
    public Member findById(Integer id);
    public Member findByTelephone(String telephone);
    public void edit(Member member);
    public Integer findMemberCountBeforeDate(String date);
    public Integer findMemberCountByDate(String date);
    public Integer findMemberCountAfterDate(String date);
    public Integer findMemberTotalCount();

    public List<Map<String,Object>> findMemberCount();


    public List<Map<String,Object>> findMemberAgeCount();
}
