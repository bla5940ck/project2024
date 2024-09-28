package com.kucw.security.dao;

import com.kucw.security.dto.CreateOrderRequest;
import com.kucw.security.model.order.Order;
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

    /** 查詢訂單
     * @param orderId
     * */
    Order getOrderById(Integer orderId);

    /** 查詢訂單細項
     * @param orderId
     * */
    List<OrderItem> getOrderItemListByOrderId(Integer orderId);
}
