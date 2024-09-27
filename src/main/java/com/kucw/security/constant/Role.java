package com.kucw.security.constant;

public enum Role {

    ROLE_ADMIN(1, "ROLE_ADMIN"),
    ROLE_NORMAL_MEMBER(2, "ROLE_NORMAL_MEMBER"),
    ROLE_VIP_MEMBER(3,"ROLE_VIP_MEMBER") ;

    private final Integer code;

    private final String name;

    Role(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public static Role getRoleByCode(Integer code) {
        for (Role role : Role.values()) {
            if (role.getCode().equals(code)) {
                return role;
            }
        }
        return null;
    }

    // 获取name
    public String getName() {
        return name;
    }

    public Integer getCode() {
        return code;
    }

}
