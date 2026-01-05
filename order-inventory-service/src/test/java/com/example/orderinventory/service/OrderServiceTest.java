package com.example.orderinventory.service;

import com.example.orderinventory.model.dto.OrderDto;
import com.example.orderinventory.model.entity.Order;
import com.example.orderinventory.repository.OrderRepository;
import com.example.orderinventory.service.impl.OrderServiceImpl;
import com.example.orderinventory.util.MapperUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    private OrderServiceImpl orderService;

    @BeforeEach
    void setUp() {
        orderService = new OrderServiceImpl(orderRepository);
    }

    @Test
    void createOrder_savesAndReturnsDto() {
        OrderDto req = new OrderDto(null, "Alice", null, null, null);

        // simulate DB assigning id and timestamps
        when(orderRepository.save(any(Order.class))).thenAnswer(inv -> {
            Order o = inv.getArgument(0);
            o.setId(100L);
            o.setCreatedAt(LocalDateTime.now());
            o.setUpdatedAt(LocalDateTime.now());
            return o;
        });

        OrderDto created = orderService.createOrder(req);

        assertThat(created).isNotNull();
        assertThat(created.getId()).isEqualTo(100L);
        assertThat(created.getStatus()).isEqualTo("PENDING");
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    void getOrderById_returnsDto() {
        Order order = new Order();
        order.setId(1L);
        order.setCustomerName("Bob");
        order.setStatus("PENDING");
        order.setCreatedAt(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        OrderDto dto = orderService.getOrderById(1L);

        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getCustomerName()).isEqualTo("Bob");
    }

    @Test
    void updateOrder_withPrimitiveLong_overloadWorks() {
        Order existing = new Order();
        existing.setId(2L);
        existing.setCustomerName("Old");
        existing.setStatus("PENDING");
        existing.setCreatedAt(LocalDateTime.now());
        existing.setUpdatedAt(LocalDateTime.now());

        when(orderRepository.findById(2L)).thenReturn(Optional.of(existing));
        when(orderRepository.save(any(Order.class))).thenAnswer(inv -> inv.getArgument(0));

        OrderDto update = new OrderDto();
        update.setCustomerName("NewName");

        OrderDto updated = orderService.updateOrder(2L, update); // primitive long overload will call boxed method

        assertThat(updated.getCustomerName()).isEqualTo("NewName");

        ArgumentCaptor<Order> captor = ArgumentCaptor.forClass(Order.class);
        verify(orderRepository).save(captor.capture());
        assertThat(captor.getValue().getCustomerName()).isEqualTo("NewName");
    }

    @Test
    void deleteOrder_withPrimitiveLong_callsRepository() {
        when(orderRepository.existsById(5L)).thenReturn(true);
        doNothing().when(orderRepository).deleteById(5L);

        orderService.deleteOrder(5L); // primitive overload calls boxed method

        verify(orderRepository).deleteById(5L);
    }
}