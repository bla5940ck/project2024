package com.kucw.security.dao;

import com.kucw.security.dto.CreateOrderRequest;
import com.kucw.security.dto.OrderQueryParams;
import com.kucw.security.model.order.Order;
import com.kucw.security.model.order.OrderItem;

import java.math.BigDecimal;
import java.util.List;

public interface OrderDao {

    /**
     * 創建訂單
     * @param memberId
     * @param totalAmount
     * */
    Integer createOrder(Integer memberId, BigDecimal totalAmount);


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


    /** 查詢訂單清單
     * @param orderQueryParams
     * */
    List<Order> getOrders(OrderQueryParams orderQueryParams);

    /**
     * 取得訂單數量
     * @param queryParams
     * */
    Integer countOrder(OrderQueryParams queryParams);
}
