package com.kucw.security.service;

import com.kucw.security.dto.CreateOrderRequest;

public interface OrderService {

    /**
     * 創建訂單
     * @param memberId
     * @param createOrderRequest
     * */
    Integer createOrder(Integer memberId, CreateOrderRequest createOrderRequest);
}
