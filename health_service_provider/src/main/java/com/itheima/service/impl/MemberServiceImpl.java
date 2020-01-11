package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.dao.MemberDao;
import com.itheima.pojo.Member;
import com.itheima.service.MemberService;
import com.itheima.utils.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service(interfaceClass =MemberService.class)
@Transactional
public class MemberServiceImpl implements MemberService {

    @Autowired
    MemberDao memberDao;
    @Override
    public Member findByTelephone(String telephone) {
        return memberDao.findByTelephone(telephone);
    }

    @Override
    public void add(Member member) {

        if(member!=null){
            String password = member.getPassword();
            String md5_password = MD5Utils.md5(password);
            member.setPassword(md5_password);
        }
        memberDao.add(member);
    }

    @Override
    public List<Integer> findMemberCountByMonth(List<String> list) {
        List<Integer> memberCount = new ArrayList<>();
        for (String members : list) {
            String data = members+".31";
            Integer memberCountBeforeDate = memberDao.findMemberCountBeforeDate(data);
            memberCount.add(memberCountBeforeDate);
        }
        return memberCount;
    }
}
