package com.kucw.security.dao;

import com.kucw.security.dto.CreateOrderRequest;
import com.kucw.security.model.order.OrderItem;

import java.util.List;

public interface OrderDao {

    /**
     * 創建訂單
     * @param memberId
     * @param totalAmount
     * */
    Integer createOrder(Integer memberId, Integer totalAmount);


    /**
     * 批量新增訂單細項
     * @param orderId
     * @param orderItems
     * */
    void createItems(Integer orderId, List<OrderItem> orderItems);
}
