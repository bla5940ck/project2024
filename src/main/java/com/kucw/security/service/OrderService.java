package com.kucw.security.service;

import com.kucw.security.dto.CreateOrderRequest;
import com.kucw.security.model.order.Order;

public interface OrderService {

    /**
     * 創建訂單
     * @param memberId
     * @param createOrderRequest
     * */
    Integer createOrder(Integer memberId, CreateOrderRequest createOrderRequest);

    /**
     * 取得訂單(含訂單細項清單)
     * @param orderId
     * */
    Order getOrderById(Integer orderId);
}
