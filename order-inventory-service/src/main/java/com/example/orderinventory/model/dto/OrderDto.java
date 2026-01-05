package com.example.orderinventory.model.dto;

import java.time.LocalDateTime;

public class OrderDto {
    private Long id;
    private String customerName;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public OrderDto() {}

    public OrderDto(Long id, String customerName, String status, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.customerName = customerName;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}