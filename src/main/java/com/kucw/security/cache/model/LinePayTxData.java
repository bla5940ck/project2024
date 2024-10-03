package com.kucw.security.cache.model;

import java.math.BigDecimal;

public class LinePayTxData extends BaseTxData{

    private long transactionId;

    private BigDecimal amount;

    private String currency;

    public long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(long transactionId) {
        this.transactionId = transactionId;
    }

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
