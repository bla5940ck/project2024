package com.kucw.security.linepay.model;

import java.math.BigDecimal;

public class BaseFormData {

    // 付款金額
    private BigDecimal amount;

    // 貨幣
    private String currency;

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
