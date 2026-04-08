package com.tadiwanashe.wms.controller;

import com.tadiwanashe.wms.entity.Order;
import com.tadiwanashe.wms.entity.OrderStatus;
import com.tadiwanashe.wms.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class OrderControllerTest {

    private MockMvc mockMvc;

    @Mock
    private OrderService orderService;

    @InjectMocks
    private OrderController orderController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(orderController).build();
    }

    @Test
    void shouldReturn201WhenOrderIsCreated() throws Exception {
        Order mockOrder = new Order();
        mockOrder.setId(1L);
        mockOrder.setCustomerId("CUST-001");
        mockOrder.setStatus(OrderStatus.PENDING);
        mockOrder.setTotalAmount(new BigDecimal("250.00"));
        mockOrder.setCreatedAt(Instant.now());

        when(orderService.createOrder("CUST-001", new BigDecimal("250.00")))
                .thenReturn(mockOrder);

        mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "customerId": "CUST-001",
                                    "totalAmount": "250.00"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("PENDING"))
                .andExpect(jsonPath("$.customerId").value("CUST-001"));
    }

    @Test
    void shouldReturn400WhenCustomerIdIsMissing() throws Exception {
        mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "customerId": "",
                                    "totalAmount": "250.00"
                                }
                                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn200WhenOrderIsFound() throws Exception {
        Order mockOrder = new Order();
        mockOrder.setId(1L);
        mockOrder.setCustomerId("CUST-001");
        mockOrder.setStatus(OrderStatus.PENDING);
        mockOrder.setTotalAmount(new BigDecimal("250.00"));
        mockOrder.setCreatedAt(Instant.now());

        when(orderService.findById(1L)).thenReturn(Optional.of(mockOrder));

        mockMvc.perform(get("/orders/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.customerId").value("CUST-001"));
    }

    @Test
    void shouldReturn404WhenOrderNotFound() throws Exception {
        when(orderService.findById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/orders/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturn200WhenStatusIsUpdated() throws Exception {
        Order updatedOrder = new Order();
        updatedOrder.setId(1L);
        updatedOrder.setCustomerId("CUST-001");
        updatedOrder.setStatus(OrderStatus.CONFIRMED);
        updatedOrder.setTotalAmount(new BigDecimal("250.00"));
        updatedOrder.setCreatedAt(Instant.now());

        when(orderService.updateStatus(1L, OrderStatus.CONFIRMED))
                .thenReturn(Optional.of(updatedOrder));

        mockMvc.perform(patch("/orders/1/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "status": "CONFIRMED"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("CONFIRMED"));
    }

    @Test
    void shouldReturn404WhenUpdatingStatusOfNonExistentOrder() throws Exception {
        when(orderService.updateStatus(99L, OrderStatus.CONFIRMED))
                .thenReturn(Optional.empty());

        mockMvc.perform(patch("/orders/99/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "status": "CONFIRMED"
                                }
                                """))
                .andExpect(status().isNotFound());
    }
}