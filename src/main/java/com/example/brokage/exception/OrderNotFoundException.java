package com.example.brokage.exception;

public class OrderNotFoundException extends RuntimeException {
    public OrderNotFoundException(){
        super("Order not found.");
    }
}
