package com.example.orderinventory.service.impl;

import com.example.orderinventory.exception.InsufficientInventoryException;
import com.example.orderinventory.exception.ResourceNotFoundException;
import com.example.orderinventory.model.dto.InventoryDto;
import com.example.orderinventory.model.entity.Inventory;
import com.example.orderinventory.repository.InventoryRepository;
import com.example.orderinventory.service.InventoryService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class InventoryServiceImpl implements InventoryService {

    private static final Logger log = LoggerFactory.getLogger(InventoryServiceImpl.class);

    private final InventoryRepository inventoryRepository;
    private final EntityManager entityManager;

    public InventoryServiceImpl(InventoryRepository inventoryRepository, EntityManager entityManager) {
        this.inventoryRepository = inventoryRepository;
        this.entityManager = entityManager;
    }

    @Override
    @Transactional(readOnly = true)
    public InventoryDto getInventoryByProductId(Long productId) {
        Optional<Inventory> opt = inventoryRepository.findById(productId);
        return opt.map(this::toDto).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public List<InventoryDto> getAllInventories() {
        return inventoryRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public InventoryDto updateInventory(Long productId, InventoryDto inventoryDto) {
        Inventory inv = inventoryRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory not found for productId=" + productId));

        if (inventoryDto.getAvailable() == null || inventoryDto.getAvailable() < 0 ||
                inventoryDto.getReserved() == null || inventoryDto.getReserved() < 0) {
            throw new IllegalArgumentException("available and reserved must be >= 0");
        }

        inv.setAvailable(inventoryDto.getAvailable());
        inv.setReserved(inventoryDto.getReserved());
        Inventory saved = inventoryRepository.save(inv);
        return toDto(saved);
    }

    @Override
    @Transactional
    public void reserveInventory(Long productId, int quantity) {
        if (quantity <= 0) throw new IllegalArgumentException("quantity must be > 0");

        Inventory inv = entityManager.find(Inventory.class, productId, LockModeType.PESSIMISTIC_WRITE);
        if (inv == null) {
            throw new ResourceNotFoundException("Inventory not found for productId=" + productId);
        }

        if (inv.getAvailable() < quantity) {
            log.warn("event=reserve outcome=failure productId={} requested={} available={}", productId, quantity, inv.getAvailable());
            throw new InsufficientInventoryException("Not enough inventory for productId=" + productId);
        }

        inv.setAvailable(inv.getAvailable() - quantity);
        inv.setReserved(inv.getReserved() + quantity);

        inventoryRepository.save(inv);
        log.info("event=reserve outcome=success productId={} qty={}", productId, quantity);
    }

    @Override
    @Transactional
    public void releaseInventory(Long productId, int quantity) {
        if (quantity <= 0) throw new IllegalArgumentException("quantity must be > 0");

        Inventory inv = entityManager.find(Inventory.class, productId, LockModeType.PESSIMISTIC_WRITE);
        if (inv == null) {
            throw new ResourceNotFoundException("Inventory not found for productId=" + productId);
        }

        int toRelease = Math.min(quantity, inv.getReserved());
        inv.setReserved(inv.getReserved() - toRelease);
        inv.setAvailable(inv.getAvailable() + toRelease);

        inventoryRepository.save(inv);
        log.info("event=release outcome=success productId={} qty={}", productId, toRelease);
    }

    private InventoryDto toDto(Inventory inv) {
        InventoryDto dto = new InventoryDto();
        dto.setProductId(inv.getProductId());
        dto.setAvailable(inv.getAvailable());
        dto.setReserved(inv.getReserved());
        dto.setPrice(null);
        return dto;
    }
}