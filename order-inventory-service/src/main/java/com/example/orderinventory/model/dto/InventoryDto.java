package com.example.orderinventory.model.dto;

import java.math.BigDecimal;

public class InventoryDto {
    private Long productId;
    private Integer available;
    private Integer reserved;
    private BigDecimal price;

    public InventoryDto() {
    }

    public InventoryDto(Long productId, Integer available, Integer reserved, BigDecimal price) {
        this.productId = productId;
        this.available = available;
        this.reserved = reserved;
        this.price = price;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Integer getAvailable() {
        return available;
    }

    public void setAvailable(Integer available) {
        this.available = available;
    }

    public Integer getReserved() {
        return reserved;
    }

    public void setReserved(Integer reserved) {
        this.reserved = reserved;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}