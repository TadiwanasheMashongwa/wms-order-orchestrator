package com.tadiwanashe.wms.repository;

import com.tadiwanashe.wms.entity.Order;
import com.tadiwanashe.wms.entity.OrderStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;

    @Test
    void shouldPersistAndRetrieveOrder() {
        Order order = new Order();
        order.setCustomerId("CUST-001");
        order.setStatus(OrderStatus.PENDING);
        order.setTotalAmount(new BigDecimal("499.99"));
        order.setCreatedAt(Instant.now());

        Order saved = orderRepository.save(order);

        Optional<Order> retrieved = orderRepository.findById(saved.getId());

        assertThat(retrieved).isPresent();
        assertThat(retrieved.get().getCustomerId()).isEqualTo("CUST-001");
        assertThat(retrieved.get().getStatus()).isEqualTo(OrderStatus.PENDING);
        assertThat(retrieved.get().getTotalAmount()).isEqualByComparingTo("499.99");
    }

    @Test
    void shouldFindOrdersByCustomerId() {
        Order order1 = new Order();
        order1.setCustomerId("CUST-002");
        order1.setStatus(OrderStatus.PENDING);
        order1.setTotalAmount(new BigDecimal("150.00"));
        order1.setCreatedAt(Instant.now());

        Order order2 = new Order();
        order2.setCustomerId("CUST-002");
        order2.setStatus(OrderStatus.CONFIRMED);
        order2.setTotalAmount(new BigDecimal("300.00"));
        order2.setCreatedAt(Instant.now());

        orderRepository.save(order1);
        orderRepository.save(order2);

        var orders = orderRepository.findByCustomerId("CUST-002");

        assertThat(orders).hasSize(2);
        assertThat(orders).allMatch(o -> o.getCustomerId().equals("CUST-002"));
    }
}