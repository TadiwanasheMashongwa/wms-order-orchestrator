package com.tadiwanashe.wms.service;

import com.tadiwanashe.wms.config.TestCacheConfig;
import com.tadiwanashe.wms.entity.Order;
import com.tadiwanashe.wms.entity.OrderStatus;
import com.tadiwanashe.wms.messaging.KafkaOrderEventPublisher;
import com.tadiwanashe.wms.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
@Import(TestCacheConfig.class)
class OrderCacheTest {

    @Autowired
    private OrderService orderService;

    @MockitoBean
    private OrderRepository orderRepository;

    @MockitoBean
    private KafkaOrderEventPublisher eventPublisher;

    @Test
    void shouldCallRepositoryOnlyOnceWhenFindByIdCalledTwice() {
        Order order = new Order();
        order.setId(1L);
        order.setCustomerId("CUST-001");
        order.setStatus(OrderStatus.PENDING);
        order.setTotalAmount(new BigDecimal("250.00"));
        order.setCreatedAt(Instant.now());

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        Optional<Order> first = orderService.findById(1L);
        Optional<Order> second = orderService.findById(1L);

        assertThat(first).isPresent();
        assertThat(second).isPresent();
        verify(orderRepository, times(1)).findById(1L);
    }
}