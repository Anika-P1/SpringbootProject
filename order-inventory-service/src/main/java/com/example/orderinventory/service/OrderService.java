package com.example.orderinventory.service;

import com.example.orderinventory.model.dto.OrderDto;

import java.util.List;

public interface OrderService {
    OrderDto createOrder(OrderDto orderDto);
    OrderDto getOrderById(Long orderId);
    List<OrderDto> getAllOrders();
    void cancelOrder(Long orderId);
    void confirmOrder(Long orderId);
    OrderDto updateOrder(Long orderId, OrderDto orderDto);
    void deleteOrder(Long orderId);
}