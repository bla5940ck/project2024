package com.kucw.security.service.impl;

import com.kucw.security.dao.OrderDao;
import com.kucw.security.dao.ProductDao;
import com.kucw.security.dto.BuyItem;
import com.kucw.security.dto.CreateOrderRequest;
import com.kucw.security.dto.OrderQueryParams;
import com.kucw.security.dto.ProductRequest;
import com.kucw.security.linepay.LinePayService;
import com.kucw.security.model.order.Order;
import com.kucw.security.model.order.OrderItem;
import com.kucw.security.model.product.Product;
import com.kucw.security.service.OrderService;
import com.kucw.security.util.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private LinePayService linePayService;

    private static final Logger log = LoggerFactory.getLogger(OrderServiceImpl.class);

    @Override
    @Transactional
    public Integer createOrder(Integer memberId, CreateOrderRequest createOrderRequest) {

        // 總花費
        BigDecimal totalAmount = BigDecimal.ZERO;

        // 訂單細項清單
        List<OrderItem> orderItems = new ArrayList<>();

        // 1.計算訂單總花費
        for (BuyItem buyItem : createOrderRequest.getBuyItemList()) {
            Integer productId = buyItem.getProductId();
            Product product = productDao.getProductById(productId);

            // 檢查商品是否存在
            if (product == null) {
                log.warn("商品 {} 不存在", productId);
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            }

            // 檢查庫存是否足夠
            if (buyItem.getQuantity() > product.getStock()) {
                log.warn("商品 {} 數量不足，無法購買，剩餘庫存:{}, 購買數量:{}", productId, product.getStock(), buyItem.getQuantity());
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            }

            // 扣除庫存
            updateStock(buyItem, product);

            BigDecimal amount = product.getPrice().multiply(new BigDecimal(buyItem.getQuantity()));
            totalAmount = totalAmount.add(amount);

            // 購買資訊 轉換 成訂單細項
            OrderItem orderItem = new OrderItem();
            orderItem.setProductId(productId);
            orderItem.setAmount(amount);
            orderItem.setPrice(product.getPrice());
            orderItem.setQuantity(buyItem.getQuantity());
            orderItem.setProductName(product.getProductName());
            orderItem.setImageUrl(product.getImageUrl());
            orderItems.add(orderItem);
        }

        // 2.新增訂單
        Integer orderId = orderDao.createOrder(memberId, totalAmount);

        // 3.新增訂單細項
        orderDao.createItems(orderId, orderItems);

        // 4.linePay 金流付款
        linePayService.requestPayment(orderId, totalAmount, memberId, orderItems);

        return orderId;

    }

    @Override
    public Order getOrderById(Integer orderId) {

        // 1.取得訂單
        Order order = orderDao.getOrderById(orderId);

        // 2.取得訂單細項清單
        List<OrderItem> orderItemList = orderDao.getOrderItemListByOrderId(orderId);

        if (!CollectionUtils.isEmpty(orderItemList)) {
            order.setOrderItemList(orderItemList);
        }

        return order;
    }

    @Override
    public Page<Order> getOrderList(OrderQueryParams orderQueryParams) {
        List<Order> orderList = orderDao.getOrders(orderQueryParams);

        if (!CollectionUtils.isEmpty(orderList)) {
            for (Order order : orderList) {
                // 查詢訂單詳細
                List<OrderItem> orderItemList = orderDao.getOrderItemListByOrderId(order.getOrderId());
                order.setOrderItemList(orderItemList);
            }
        }

        // 取得訂單數量
        Integer totalQuantity = orderDao.countOrder(orderQueryParams);

        Page<Order> page = new Page<>();
        page.setDataList(orderList);
        page.setTotal(totalQuantity);
        page.setLimit(orderQueryParams.getLimit());
        page.setOffset(orderQueryParams.getOffset());

        return page;
    }

    private void updateStock(BuyItem buyItem, Product product) {
        int stock = product.getStock() - buyItem.getQuantity();
        product.setStock(stock);
        ProductRequest productRequest = getProductRequest(product);
        productDao.updateProduct(product.getProductId(), productRequest);
    }

    private ProductRequest getProductRequest(Product product) {
        ProductRequest productRequest = new ProductRequest();
        productRequest.setProductName(product.getProductName());
        productRequest.setCategory(product.getCategory());
        productRequest.setPrice(product.getPrice());
        productRequest.setDescription(product.getDescription());
        productRequest.setStock(product.getStock());
        productRequest.setImageUrl(product.getImageUrl());
        return productRequest;
    }
}
