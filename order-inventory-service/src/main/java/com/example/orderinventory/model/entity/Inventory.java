package com.example.orderinventory.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "inventory")
public class Inventory {

    @Id
    private Long productId;
    private Integer available;
    private Integer reserved;

    public Inventory() {
    }

    public Inventory(Long productId, Integer available, Integer reserved) {
        this.productId = productId;
        this.available = available;
        this.reserved = reserved;
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
}