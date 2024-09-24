package com.kucw.security.dao;

import com.kucw.security.model.Member;
import com.kucw.security.model.Role;

import java.util.List;

public interface MemberDao {

    Member getMemberByEmail(String email);

    Integer createMember(Member member);

    Member getMemberById(Integer id);

    List<Role> getRolesByMemberId(Integer memberId);

}
