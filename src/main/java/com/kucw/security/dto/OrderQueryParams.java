package com.kucw.security.dto;

public class OrderQueryParams {

    private Integer memberId;

    // 分頁 (一次呈現多少數據)
    private Integer limit;

    // 分頁 跳過筆數
    private Integer offset;

    public Integer getMemberId() {
        return memberId;
    }

    public void setMemberId(Integer memberId) {
        this.memberId = memberId;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }
}
