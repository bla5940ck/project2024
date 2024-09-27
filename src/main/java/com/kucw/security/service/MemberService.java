package com.kucw.security.service;

import com.kucw.security.dto.MemberRegisterRequest;
import com.kucw.security.model.member.Member;

public interface MemberService {

    // 註冊會員
    Member register(MemberRegisterRequest memberRegisterRequest);
}
