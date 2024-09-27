package com.kucw.security.dao;

import com.kucw.security.model.role.MemberHasRole;

import java.util.List;

public interface RoleDao {

    /** 創建會員與角色權限
     * @param memberHasRoleList
     * */
    void createMemberHasRole(List<MemberHasRole> memberHasRoleList);
}
