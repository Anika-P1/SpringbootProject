package com.example.orderinventory.controller;

import com.example.orderinventory.model.dto.InventoryDto;
import com.example.orderinventory.service.InventoryService;
import com.example.orderinventory.exception.ResourceNotFoundException;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/inventory")
@Validated
public class InventoryController {

    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @GetMapping("/{productId}")
    public ResponseEntity<InventoryDto> getByProductId(@PathVariable Long productId) {
        InventoryDto dto = inventoryService.getInventoryByProductId(productId);
        if (dto == null) {
            throw new ResourceNotFoundException("Inventory not found for productId=" + productId);
        }
        return ResponseEntity.ok(dto);
    }

    @GetMapping
    public ResponseEntity<List<InventoryDto>> listAll() {
        return ResponseEntity.ok(inventoryService.getAllInventories());
    }

    @PutMapping("/{productId}")
    public ResponseEntity<InventoryDto> update(
            @PathVariable Long productId,
            @Valid @RequestBody InventoryDto inventoryDto) {

        InventoryDto updated = inventoryService.updateInventory(productId, inventoryDto);
        return ResponseEntity.ok(updated);
    }

    @PostMapping("/{productId}/reserve")
    public ResponseEntity<Void> reserve(@PathVariable Long productId, @RequestParam int quantity) {
        inventoryService.reserveInventory(productId, quantity);
        return ResponseEntity.created(URI.create("/inventory/" + productId)).build();
    }

    @PostMapping("/{productId}/release")
    public ResponseEntity<Void> release(@PathVariable Long productId, @RequestParam int quantity) {
        inventoryService.releaseInventory(productId, quantity);
        return ResponseEntity.ok().build();
    }
}