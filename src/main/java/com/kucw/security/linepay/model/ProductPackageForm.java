package com.kucw.security.linepay.model;

import java.math.BigDecimal;
import java.util.List;

public class ProductPackageForm {

    private String id;

    private String name;

    // 商品總價
    private BigDecimal amount;

    private List<ProductForm> products;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public List<ProductForm> getProducts() {
        return products;
    }

    public void setProducts(List<ProductForm> products) {
        this.products = products;
    }
}
