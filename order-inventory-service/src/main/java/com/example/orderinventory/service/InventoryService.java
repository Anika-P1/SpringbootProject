package com.example.orderinventory.service;

import com.example.orderinventory.model.dto.InventoryDto;

import java.util.List;

public interface InventoryService {
    InventoryDto getInventoryByProductId(Long productId);
    List<InventoryDto> getAllInventories(); // renamed to match implementation
    InventoryDto updateInventory(Long productId, InventoryDto inventoryDto);
    void reserveInventory(Long productId, int quantity);
    void releaseInventory(Long productId, int quantity);
}