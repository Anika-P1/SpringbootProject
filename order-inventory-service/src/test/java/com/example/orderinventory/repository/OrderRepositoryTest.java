package com.example.orderinventory.repository;

import com.example.orderinventory.model.entity.Order;
import com.example.orderinventory.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@ActiveProfiles("test")
public class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;

    private Order order;

    @BeforeEach
    public void setUp() {
        order = new Order();
        order.setStatus("CREATED");
        order.setCreatedAt(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());
        orderRepository.save(order);
    }

    @Test
    public void testFindById() {
        Optional<Order> foundOrder = orderRepository.findById(order.getId());
        assertThat(foundOrder).isPresent();
        assertThat(foundOrder.get().getStatus()).isEqualTo(order.getStatus());
    }

    @Test
    public void testDeleteById() {
        Long orderId = order.getId();
        orderRepository.deleteById(orderId);
        Optional<Order> deletedOrder = orderRepository.findById(orderId);
        assertThat(deletedOrder).isNotPresent();
    }

    @Test
    public void testSaveOrder() {
        Order newOrder = new Order();
        newOrder.setStatus("CONFIRMED");
        newOrder.setCreatedAt(LocalDateTime.now());
        newOrder.setUpdatedAt(LocalDateTime.now());
        Order savedOrder = orderRepository.save(newOrder);
        assertThat(savedOrder).isNotNull();
        assertThat(savedOrder.getId()).isNotNull();
        assertThat(savedOrder.getStatus()).isEqualTo("CONFIRMED");
    }
}