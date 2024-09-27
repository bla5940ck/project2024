package com.kucw.security.service.impl;

import com.kucw.security.constant.Role;
import com.kucw.security.dao.MemberDao;
import com.kucw.security.dao.RoleDao;
import com.kucw.security.dto.MemberRegisterRequest;
import com.kucw.security.model.member.Member;
import com.kucw.security.model.role.MemberHasRole;
import com.kucw.security.service.MemberService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Component
public class MemberServiceImpl implements MemberService {

    @Autowired
    private MemberDao memberDao;

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private final static Logger log = LoggerFactory.getLogger(MemberServiceImpl.class);

    @Transactional
    public Member register(MemberRegisterRequest memberRegisterRequest) {
        // 檢查帳號是否被使用過
        Member member = memberDao.getMemberByEmail(memberRegisterRequest.getEmail());
        if (member != null) {
            log.warn("該email{}，已被註冊。", memberRegisterRequest.getEmail());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        // hash 原始密碼
        String encode = passwordEncoder.encode(memberRegisterRequest.getPassword());
        memberRegisterRequest.setPassword(encode);

        // 創建會員
        Integer memberId = null;
        try {
            memberId = memberDao.createMember(memberRegisterRequest);
        } catch (Exception e) {
            log.error("插入資料庫有誤: {},{} ",e.getClass(), e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        log.info("register new memberId: {}", memberId);

        // 查詢會員
        Member memberById = null;
        try {
            memberById = memberDao.getMemberById(memberId);
        } catch (Exception e) {
            log.error("查詢會員資料有誤: {},{} ",e.getClass(), e.getMessage());
        }

        // 給予一般會員權限 ROLE_NORMAL_MEMBER(role_id = 2)
        try {
            List<MemberHasRole> memberHasRoleList = new ArrayList<>();
            memberHasRoleList.add(new MemberHasRole(memberId, Role.ROLE_NORMAL_MEMBER.getCode()));
            roleDao.createMemberHasRole(memberHasRoleList);
        } catch (Exception e) {
            log.error("權限設定錯誤 e: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY);
        }

        return memberById;
    }

}
