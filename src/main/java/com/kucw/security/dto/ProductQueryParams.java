package com.kucw.security.dto;

import com.kucw.security.constant.ProductCategory;

public class ProductQueryParams {

    // 商品類型
    private ProductCategory category;

    // 查詢框(商品名稱)
    private String search;

    // 排序方式
    private String orderBy;

    // 排序順序(生冪/降冪)
    private String sort;

    // 分頁 (一次呈現多少數據)
    private Integer limit;

    // 分頁 跳過筆數
    private Integer offset;

    public ProductCategory getCategory() {
        return category;
    }

    public void setCategory(ProductCategory category) {
        this.category = category;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
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
