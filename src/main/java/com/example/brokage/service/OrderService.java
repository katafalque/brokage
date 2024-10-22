package com.example.brokage.service;

import com.example.brokage.data.dto.OrderDto;
import com.example.brokage.data.request.CreateOrderRequest;
import com.example.brokage.data.request.ListOrdersRequest;
import com.example.brokage.data.response.ListOrdersResponse;

import java.util.Date;

public interface OrderService {
    void createOrder(String email, CreateOrderRequest createOrderRequest);

    void deleteOrder(String email, String orderId);

    ListOrdersResponse<OrderDto> getByFilter(String email, ListOrdersRequest request);
}
