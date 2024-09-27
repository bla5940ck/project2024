package com.kucw.security.dao;

import com.kucw.security.dto.MemberRegisterRequest;
import com.kucw.security.model.member.Member;
import com.kucw.security.model.role.Role;

import java.util.List;

public interface MemberDao {

    Member getMemberByEmail(String email);

    Integer createMember(MemberRegisterRequest memberRegisterRequest);

    Member getMemberById(Integer id);

    List<Role> getRolesByMemberId(Integer memberId);

}
