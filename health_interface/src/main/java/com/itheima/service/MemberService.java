package com.itheima.service;

import com.itheima.pojo.Member;

import java.util.List;
import java.util.Map;

public interface MemberService {
    Member findByTelephone(String telephone);

    void add(Member member);

    List<Integer> findMemberCountByMonth(List<String> list);

    public List<Map<String,Object>> findMemberCount();


    public List<Map<String,Object>> findMemberAgeCount();
}
