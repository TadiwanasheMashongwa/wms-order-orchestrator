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
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private KafkaOrderEventPublisher eventPublisher;

    @InjectMocks
    private OrderService orderService;

    @Test
    void shouldCreateOrderWithPendingStatus() {
        Order savedOrder = new Order();
        savedOrder.setId(1L);
        savedOrder.setCustomerId("CUST-001");
        savedOrder.setStatus(OrderStatus.PENDING);
        savedOrder.setTotalAmount(new BigDecimal("250.00"));
        savedOrder.setCreatedAt(Instant.now());

        when(orderRepository.save(any(Order.class))).thenReturn(savedOrder);

        Order result = orderService.createOrder("CUST-001", new BigDecimal("250.00"));

        assertThat(result.getStatus()).isEqualTo(OrderStatus.PENDING);
        assertThat(result.getCustomerId()).isEqualTo("CUST-001");
    }

    @Test
    void shouldUpdateOrderStatusToConfirmed() {
        Order existingOrder = new Order();
        existingOrder.setId(1L);
        existingOrder.setCustomerId("CUST-001");
        existingOrder.setStatus(OrderStatus.PENDING);
        existingOrder.setTotalAmount(new BigDecimal("250.00"));
        existingOrder.setCreatedAt(Instant.now());

        Order updatedOrder = new Order();
        updatedOrder.setId(1L);
        updatedOrder.setCustomerId("CUST-001");
        updatedOrder.setStatus(OrderStatus.CONFIRMED);
        updatedOrder.setTotalAmount(new BigDecimal("250.00"));
        updatedOrder.setCreatedAt(existingOrder.getCreatedAt());

        when(orderRepository.findById(1L)).thenReturn(Optional.of(existingOrder));
        when(orderRepository.save(any(Order.class))).thenReturn(updatedOrder);

        Optional<Order> result = orderService.updateStatus(1L, OrderStatus.CONFIRMED);

        assertThat(result).isPresent();
        assertThat(result.get().getStatus()).isEqualTo(OrderStatus.CONFIRMED);
        assertThat(result.get().getCustomerId()).isEqualTo("CUST-001");
    }
}