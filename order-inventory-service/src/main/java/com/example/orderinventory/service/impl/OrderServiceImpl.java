package com.example.orderinventory.service.impl;

import com.example.orderinventory.exception.ResourceNotFoundException;
import com.example.orderinventory.model.dto.OrderDto;
import com.example.orderinventory.model.entity.Order;
import com.example.orderinventory.repository.OrderRepository;
import com.example.orderinventory.service.OrderService;
import com.example.orderinventory.util.MapperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public OrderDto createOrder(OrderDto orderDto) {
        Order order = MapperUtils.toOrderEntity(orderDto);
        if (order.getStatus() == null || order.getStatus().isBlank()) {
            order.setStatus("PENDING");
        }
        Order saved = orderRepository.save(order);
        return MapperUtils.toOrderDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderDto getOrderById(Long id) {
        return orderRepository.findById(id)
                .map(MapperUtils::toOrderDto)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found id=" + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderDto> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(MapperUtils::toOrderDto)
                .toList();
    }

    @Override
    public void cancelOrder(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found id=" + id));
        if ("CANCELLED".equalsIgnoreCase(order.getStatus())) {
            return;
        }
        order.setStatus("CANCELLED");
        order.setUpdatedAt(LocalDateTime.now());
        orderRepository.save(order);
    }

    @Override
    public void confirmOrder(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found id=" + id));
        if ("CONFIRMED".equalsIgnoreCase(order.getStatus())) {
            return;
        }
        if ("CANCELLED".equalsIgnoreCase(order.getStatus())) {
            throw new IllegalStateException("Cannot confirm a cancelled order id=" + id);
        }
        order.setStatus("CONFIRMED");
        order.setUpdatedAt(LocalDateTime.now());
        orderRepository.save(order);
    }

    @Override
    public OrderDto updateOrder(Long id, OrderDto orderDto) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found id=" + id));
        if (orderDto.getCustomerName() != null) {
            order.setCustomerName(orderDto.getCustomerName());
        }
        if (orderDto.getStatus() != null) {
            order.setStatus(orderDto.getStatus());
        }
        order.setUpdatedAt(LocalDateTime.now());
        Order saved = orderRepository.save(order);
        return MapperUtils.toOrderDto(saved);
    }

    // primitive overloads used by some tests - do NOT use @Override
    public OrderDto updateOrder(long id, OrderDto orderDto) {
        return updateOrder(Long.valueOf(id), orderDto);
    }

    @Override
    public void deleteOrder(Long id) {
        if (!orderRepository.existsById(id)) {
            throw new ResourceNotFoundException("Order not found id=" + id);
        }
        orderRepository.deleteById(id);
    }

    // primitive overload
    public void deleteOrder(long id) {
        deleteOrder(Long.valueOf(id));
    }
}