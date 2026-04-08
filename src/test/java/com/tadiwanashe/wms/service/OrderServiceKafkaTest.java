package com.tadiwanashe.wms.service;

import com.tadiwanashe.wms.entity.Order;
import com.tadiwanashe.wms.entity.OrderStatus;
import com.tadiwanashe.wms.messaging.KafkaOrderEventPublisher;
import com.tadiwanashe.wms.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.Instant;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceKafkaTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private KafkaOrderEventPublisher eventPublisher;

    @InjectMocks
    private OrderService orderService;

    @Test
    void shouldPublishEventWhenOrderIsCreated() {
        Order savedOrder = new Order();
        savedOrder.setId(1L);
        savedOrder.setCustomerId("CUST-001");
        savedOrder.setStatus(OrderStatus.PENDING);
        savedOrder.setTotalAmount(new BigDecimal("250.00"));
        savedOrder.setCreatedAt(Instant.now());

        when(orderRepository.save(any(Order.class))).thenReturn(savedOrder);

        orderService.createOrder("CUST-001", new BigDecimal("250.00"));

        verify(eventPublisher).publishOrderCreated(savedOrder);
    }
}