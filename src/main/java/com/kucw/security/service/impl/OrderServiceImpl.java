package com.kucw.security.service.impl;

import com.kucw.security.dao.OrderDao;
import com.kucw.security.dao.ProductDao;
import com.kucw.security.dto.BuyItem;
import com.kucw.security.dto.CreateOrderRequest;
import com.kucw.security.model.order.OrderItem;
import com.kucw.security.model.product.Product;
import com.kucw.security.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private ProductDao productDao;

    @Override
    @Transactional
    public Integer createOrder(Integer memberId, CreateOrderRequest createOrderRequest) {

        // 總花費
        int totalAmount = 0;

        // 訂單細項清單
        List<OrderItem> orderItems = new ArrayList<>();

        // 1.計算訂單總花費
        for (BuyItem buyItem : createOrderRequest.getBuyItemList()) {
            Product product = productDao.getProductById(buyItem.getProductId());

            int amount = buyItem.getQuantity() * product.getPrice();
            totalAmount += amount;

            // 購買資訊 轉換 成訂單細項
            OrderItem orderItem = new OrderItem();
            orderItem.setProductId(buyItem.getProductId());
            orderItem.setAmount(amount);
            orderItem.setQuantity(buyItem.getQuantity());
            orderItems.add(orderItem);
        }

        // 2.新增訂單
        Integer orderId = orderDao.createOrder(memberId, totalAmount);

        // 3.新增訂單細項
        orderDao.createItems(orderId, orderItems);

        return orderId;

    }
}
