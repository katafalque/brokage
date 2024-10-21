package com.example.brokage.service.impl;

import com.example.brokage.data.enums.Side;
import com.example.brokage.data.enums.Status;
import com.example.brokage.data.request.CreateOrderRequest;
import com.example.brokage.entity.Order;
import com.example.brokage.entity.User;
import com.example.brokage.repository.UserRepository;
import com.example.brokage.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class OrderServiceImpl implements OrderService {
    private final UserRepository userRepository;

    @Autowired
    public OrderServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void createOrder(CreateOrderRequest createOrderRequest) {
        User user = userRepository.findById(UUID.fromString(createOrderRequest.getCustomerId())).orElseThrow();

        Order order = Order.builder()
                .orderSide(Side.fromValue(createOrderRequest.getSide()))
                .assetName(createOrderRequest.getAsset())
                .price(createOrderRequest.getPrice())
                .size(createOrderRequest.getSize())
                .status(Status.PENDING)
                .build();

        user.addOrder(order);

        this.userRepository.save(user);
    }
}
