package com.kucw.security.model.role;

public class MemberHasRole {

    // 會員Id
    private Integer memberId;

    // 角色Id
    private Integer roleId;

    public Integer getMemberId() {
        return memberId;
    }

    public void setMemberId(Integer memberId) {
        this.memberId = memberId;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public MemberHasRole(Integer memberId, Integer roleId) {
        this.memberId = memberId;
        this.roleId = roleId;
    }

    public MemberHasRole() {
    }
}
