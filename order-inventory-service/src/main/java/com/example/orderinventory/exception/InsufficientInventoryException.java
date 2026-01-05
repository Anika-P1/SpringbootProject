package com.example.orderinventory.exception;

/**
 * Thrown when a reservation cannot be fulfilled due to insufficient available inventory.
 */
public class InsufficientInventoryException extends RuntimeException {
    public InsufficientInventoryException() { super(); }
    public InsufficientInventoryException(String message) { super(message); }
    public InsufficientInventoryException(String message, Throwable cause) { super(message, cause); }
}