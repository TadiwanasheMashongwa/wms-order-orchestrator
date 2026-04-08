package com.tadiwanashe.wms.service;

import com.tadiwanashe.wms.entity.Order;
import com.tadiwanashe.wms.entity.OrderStatus;
import com.tadiwanashe.wms.messaging.KafkaOrderEventPublisher;
import com.tadiwanashe.wms.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final KafkaOrderEventPublisher eventPublisher;

    public OrderService(OrderRepository orderRepository, KafkaOrderEventPublisher eventPublisher) {
        this.orderRepository = orderRepository;
        this.eventPublisher = eventPublisher;
    }

    public Order createOrder(String customerId, BigDecimal totalAmount) {
        Order order = new Order();
        order.setCustomerId(customerId);
        order.setStatus(OrderStatus.PENDING);
        order.setTotalAmount(totalAmount);
        order.setCreatedAt(Instant.now());
        Order saved = orderRepository.save(order);
        eventPublisher.publishOrderCreated(saved);
        return saved;
    }

    public Optional<Order> findById(Long id) {
        return orderRepository.findById(id);
    }

    public Optional<Order> updateStatus(Long id, OrderStatus newStatus) {
        return orderRepository.findById(id).map(order -> {
            order.setStatus(newStatus);
            return orderRepository.save(order);
        });
    }
}