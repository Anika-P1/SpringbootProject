package com.example.orderinventory.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.http.HttpStatus;

import java.time.OffsetDateTime;

/**
 * Simple problem-like error body returned by the GlobalExceptionHandler.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiError {
    private final String title;
    private final int status;
    private final String detail;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private final OffsetDateTime timestamp = OffsetDateTime.now();

    public ApiError(HttpStatus status, String detail) {
        this.title = status.getReasonPhrase();
        this.status = status.value();
        this.detail = detail;
    }

    public String getTitle() { return title; }
    public int getStatus() { return status; }
    public String getDetail() { return detail; }
    public OffsetDateTime getTimestamp() { return timestamp; }
}