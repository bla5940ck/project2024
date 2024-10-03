package com.kucw.security.dto;

import com.kucw.security.linepay.model.PaymentUrl;
import com.kucw.security.model.order.Order;

public class OrderData {

    private Integer orderId;

    private Order order;

    private PaymentUrl paymentUrl;



    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public PaymentUrl getPaymentUrl() {
        return paymentUrl;
    }

    public void setPaymentUrl(PaymentUrl paymentUrl) {
        this.paymentUrl = paymentUrl;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}
