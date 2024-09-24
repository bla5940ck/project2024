package com.kucw.security.service.impl;

import com.kucw.security.dao.MemberDao;
import com.kucw.security.model.Member;
import com.kucw.security.service.MemberService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class MemberServiceImpl implements MemberService {

    @Autowired
    private MemberDao memberDao;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private final static Logger log = LoggerFactory.getLogger(MemberServiceImpl.class);

    @Transactional
    public Member register(Member member) {
        // 檢查帳號是否被使用過

        // hash 原始密碼
        String encode = passwordEncoder.encode(member.getPassword());
        member.setPassword(encode);

        // 在資料庫插入member資料
        Integer memberId = null;
        try {
            memberId = memberDao.createMember(member);        } catch (Exception e) {
            log.error("插入資料庫有誤: {},{} ",e.getClass(), e.getMessage());
        }

        log.info("register new memberId: {}", memberId);

        Member memberById = null;
        try {
          member = memberDao.getMemberById(memberId);
        } catch (Exception e) {
            log.error("查詢會員資料有誤: {},{} ",e.getClass(), e.getMessage());
        }

        return member;
    }

}
