package com.example.brokage.service;

import com.example.brokage.data.request.CreateOrderRequest;

public interface OrderService {
    void createOrder(CreateOrderRequest createOrderRequest);
}
