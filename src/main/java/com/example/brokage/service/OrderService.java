package com.example.brokage.service;

import com.example.brokage.data.request.CreateOrderRequest;

public interface OrderService {
    void createOrder(String email, CreateOrderRequest createOrderRequest);

    void deleteOrder(String email, String orderId);
}
