package com.example.orderinventory.event;

import org.springframework.context.ApplicationEvent;

public class DomainEvents<T> extends ApplicationEvent {
    private final T data;

    public DomainEvents(Object source, T data) {
        super(source);
        this.data = data;
    }

    public T getData() {
        return data;
    }
}