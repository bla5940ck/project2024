package com.kucw.security.util;

import java.util.List;

/** 分頁通用 */
public class Page<T> {

    // 分頁 (一次呈現多少數據)
    private Integer limit;

    // 分頁 跳過筆數
    private Integer offset;

    // 總比數
    private Integer total;

    // 資料清單
    private List<T> dataList;

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

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public List<T> getDataList() {
        return dataList;
    }

    public void setDataList(List<T> dataList) {
        this.dataList = dataList;
    }
}
