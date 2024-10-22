package com.example.brokage.service.impl;

import com.example.brokage.data.dto.OrderDto;
import com.example.brokage.data.enums.Side;
import com.example.brokage.data.enums.Status;
import com.example.brokage.data.request.CreateOrderRequest;
import com.example.brokage.data.request.ListOrdersRequest;
import com.example.brokage.data.response.ListOrdersResponse;
import com.example.brokage.data.specification.OrderSpecification;
import com.example.brokage.entity.Asset;
import com.example.brokage.entity.Order;
import com.example.brokage.entity.User;
import com.example.brokage.exception.InsufficientBalanceException;
import com.example.brokage.exception.OrderNotFoundException;
import com.example.brokage.exception.WrongOrderStatusException;
import com.example.brokage.mapper.ListOrdersResponseMapper;
import com.example.brokage.repository.OrderRepository;
import com.example.brokage.repository.UserRepository;
import com.example.brokage.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class OrderServiceImpl implements OrderService {
    private static final String TRY = "TRY";
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final ListOrdersResponseMapper listOrdersResponseMapper;

    @Autowired
    public OrderServiceImpl(UserRepository userRepository, OrderRepository orderRepository, ListOrdersResponseMapper listOrdersResponseMapper) {
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
        this.listOrdersResponseMapper = listOrdersResponseMapper;
    }

    @Override
    public void createOrder(String email, CreateOrderRequest createOrderRequest) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found."));

        Set<Asset> assets = user.getAssets();

        if (assets == null || assets.isEmpty()) throw new InsufficientBalanceException();
        if (Side.fromValue(createOrderRequest.getSide()) == Side.BUY){
            handleCreateBuyOrder(user, createOrderRequest);
        } else {
            handleCreateSellOrder(user, createOrderRequest);
        }
    }

    @Override
    public void deleteOrder(String email, String orderId) {
        Order order = this.orderRepository.findByUserEmailAndOrderId(email, orderId)
                .orElseThrow(OrderNotFoundException::new);

        if (order.getStatus() == Status.PENDING)
            orderRepository.delete(order);
        else
            throw new WrongOrderStatusException();
    }

    @Override
    public ListOrdersResponse<OrderDto> getByFilter(String email, ListOrdersRequest request) {
        User user = this.userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found."));

        Specification<Order> spec =
                OrderSpecification.filterBy(user.getId().toString(), request.getStartDate(), request.getEndDate());

        Page<Order> result = this.orderRepository.findAll(
                spec,
                PageRequest.of(request.getPage(), request.getPageSize())
        );

        return this.listOrdersResponseMapper.toListOrdersResponse(result);
    }

    private void handleCreateBuyOrder(User user, CreateOrderRequest createOrderRequest) {
        Set<Asset> assets = user.getAssets();
        Asset asset = getAssetByName(TRY, assets);

        if (
                asset == null ||
                asset.getUsableSize() < createOrderRequest.getPrice() * createOrderRequest.getSize()
        ) throw new InsufficientBalanceException();

        Order order = Order.builder()
                .orderSide(Side.BUY)
                .assetName(createOrderRequest.getAsset())
                .price(createOrderRequest.getPrice())
                .size(createOrderRequest.getSize())
                .status(Status.PENDING)
                .build();

        asset.setUsableSize(asset.getUsableSize() - (createOrderRequest.getSize() * createOrderRequest.getPrice()));
        user.addOrder(order);
        this.userRepository.save(user);
    }

    private void handleCreateSellOrder(User user, CreateOrderRequest createOrderRequest) {
        Set<Asset> assets = user.getAssets();
        Asset asset = getAssetByName(createOrderRequest.getAsset(), assets);

        if (
                asset == null ||
                asset.getUsableSize() < createOrderRequest.getSize()
        ) throw new InsufficientBalanceException();

        Order order = Order.builder()
                .orderSide(Side.SELL)
                .assetName(createOrderRequest.getAsset())
                .price(createOrderRequest.getPrice())
                .size(createOrderRequest.getSize())
                .status(Status.PENDING)
                .build();

        user.addOrder(order);
        asset.setUsableSize(asset.getUsableSize() - createOrderRequest.getSize());
        this.userRepository.save(user);
    }

    private Asset getAssetByName(String assetName, Set<Asset> assets) {
        for (Asset asset : assets) {
            if (asset.getAssetName().equals(assetName)) {
                return asset;
            }
        }
        return null;
    }
}
