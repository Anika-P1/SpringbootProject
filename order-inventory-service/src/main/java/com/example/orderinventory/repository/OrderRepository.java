package com.example.orderinventory.repository;

import com.example.orderinventory.model.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    // Additional query methods can be defined here if needed
}