package com.example.orderinventory.util;

import com.example.orderinventory.model.dto.OrderDto;
import com.example.orderinventory.model.dto.InventoryDto;
import com.example.orderinventory.model.entity.Order;
import com.example.orderinventory.model.entity.Inventory;

public class MapperUtils {

    public static OrderDto toOrderDto(Order order) {
        if (order == null) return null;
        OrderDto orderDto = new OrderDto();
        orderDto.setId(order.getId());
        orderDto.setCustomerName(order.getCustomerName());
        orderDto.setStatus(order.getStatus());
        orderDto.setCreatedAt(order.getCreatedAt());
        orderDto.setUpdatedAt(order.getUpdatedAt());
        return orderDto;
    }

    public static Order toOrderEntity(OrderDto orderDto) {
        if (orderDto == null) return null;
        Order order = new Order();
        if (orderDto.getId() != null) order.setId(orderDto.getId());
        order.setCustomerName(orderDto.getCustomerName());
        if (orderDto.getStatus() != null) order.setStatus(orderDto.getStatus());
        if (orderDto.getCreatedAt() != null) order.setCreatedAt(orderDto.getCreatedAt());
        if (orderDto.getUpdatedAt() != null) order.setUpdatedAt(orderDto.getUpdatedAt());
        return order;
    }

    public static InventoryDto toInventoryDto(Inventory inventory) {
        if (inventory == null) return null;
        InventoryDto dto = new InventoryDto();
        dto.setProductId(inventory.getProductId());
        dto.setAvailable(inventory.getAvailable());
        dto.setReserved(inventory.getReserved());
        return dto;
    }

    public static Inventory toInventoryEntity(InventoryDto dto) {
        if (dto == null) return null;
        Inventory inv = new Inventory();
        inv.setProductId(dto.getProductId());
        inv.setAvailable(dto.getAvailable());
        inv.setReserved(dto.getReserved());
        return inv;
    }
}