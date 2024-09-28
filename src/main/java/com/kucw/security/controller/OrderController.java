package com.kucw.security.controller;

import com.kucw.security.dto.CreateOrderRequest;
import com.kucw.security.dto.OrderQueryParams;
import com.kucw.security.model.order.Order;
import com.kucw.security.service.OrderService;
import com.kucw.security.util.Page;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Validated
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/members/{memberId}/orders")
    public ResponseEntity<Order> createOrder(@PathVariable Integer memberId,
                                         @RequestBody @Valid CreateOrderRequest createOrderRequest) {

        Integer orderId = orderService.createOrder(memberId, createOrderRequest);

        Order order = orderService.getOrderById(orderId);

        return ResponseEntity.status(HttpStatus.CREATED).body(order);
    }

    @GetMapping("members/{memberId}/orders")
    public ResponseEntity<Page<Order>> getOrders(
            @PathVariable Integer memberId,
            // 分頁 Pagination
            @RequestParam(defaultValue = "10") @Max(1000) @Min(0) Integer limit,
            @RequestParam(defaultValue = "0") @Min(0) Integer offset) {

        // 1.組合參數
        OrderQueryParams queryParams = new OrderQueryParams();
        queryParams.setLimit(limit);
        queryParams.setOffset(offset);
        queryParams.setMemberId(memberId);

        // 2.取得訂單清單
        Page<Order> orderList = orderService.getOrderList(queryParams);

        return ResponseEntity.status(HttpStatus.OK).body(orderList);
    }
}
