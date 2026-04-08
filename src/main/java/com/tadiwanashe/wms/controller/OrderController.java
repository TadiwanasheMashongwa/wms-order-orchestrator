package com.tadiwanashe.wms.controller;

import com.tadiwanashe.wms.entity.Order;
import com.tadiwanashe.wms.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody CreateOrderRequest request) {
        Order order = orderService.createOrder(request.customerId(), request.totalAmount());
        return ResponseEntity.status(HttpStatus.CREATED).body(order);
    }

    public record CreateOrderRequest(String customerId, BigDecimal totalAmount) {}
}