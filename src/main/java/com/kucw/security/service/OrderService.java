package com.kucw.security.service;

import com.kucw.security.dto.CreateOrderRequest;
import com.kucw.security.dto.OrderQueryParams;
import com.kucw.security.model.order.Order;
import com.kucw.security.util.Page;

import java.util.List;

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

    /** 取得訂單清單(分頁) */
    Page<Order> getOrderList(OrderQueryParams orderQueryParams);
}
