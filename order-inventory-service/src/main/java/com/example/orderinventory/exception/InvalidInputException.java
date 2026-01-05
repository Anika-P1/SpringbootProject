package com.example.orderinventory.exception;

/**
 * Thrown when input validation fails (bad request).
 */
public class InvalidInputException extends RuntimeException {
    public InvalidInputException() { super(); }
    public InvalidInputException(String message) { super(message); }
    public InvalidInputException(String message, Throwable cause) { super(message, cause); }
}