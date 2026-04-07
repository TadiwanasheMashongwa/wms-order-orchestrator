package com.tadiwanashe.wms.repository;

import com.tadiwanashe.wms.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order,Long> {
    List<Order>  findByCustomerId(String CustomerId);
    List<Order> findByStatus(String Status);
}
