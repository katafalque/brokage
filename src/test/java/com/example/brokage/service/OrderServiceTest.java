package com.example.brokage.service;

import com.example.brokage.data.enums.Side;
import com.example.brokage.data.enums.Status;
import com.example.brokage.data.request.CreateOrderRequest;
import com.example.brokage.entity.Asset;
import com.example.brokage.entity.Order;
import com.example.brokage.entity.User;
import com.example.brokage.exception.InsufficientBalanceException;
import com.example.brokage.exception.OrderNotFoundException;
import com.example.brokage.exception.WrongOrderStatusException;
import com.example.brokage.repository.OrderRepository;
import com.example.brokage.repository.UserRepository;
import com.example.brokage.service.impl.OrderServiceImpl;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class OrderServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderServiceImpl orderService; // The class that contains createOrder

    private static Faker faker;

    @BeforeEach
    void setUp() {
        faker = new Faker();
    }

    @Test
    void should_throw_user_not_found_ex() {
        /* Arrange */
        var email = faker.internet().emailAddress();
        var request = CreateOrderRequest.builder().build();

        /* Act & Assert */
        Exception exception = assertThrows(UsernameNotFoundException.class, () ->
                orderService.createOrder(email, request)
        );

        assertEquals("User not found.", exception.getMessage());
    }


    @Test
    void should_throw_insufficient_balance_ex_when_no_assets() {
        /* Arrange */
        var email = faker.internet().emailAddress();
        var request = CreateOrderRequest.builder().build();
        var user = User.builder().build();

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        /* Act & Assert */
        Exception exception = assertThrows(InsufficientBalanceException.class, () ->
                orderService.createOrder(email, request)
        );

        assertEquals("Insufficient balance", exception.getMessage());
    }


    @Test
    void should_throw_insufficient_balance_ex_when_not_enough_size() {
        /* Arrange */
        var email = faker.internet().emailAddress();
        var request = CreateOrderRequest.builder()
                .asset("USD")
                .price(10)
                .size(1000)
                .build();
        var user = User.builder().build();
        var asset = Asset.builder()
                .assetName("TRY")
                .usableSize(1000)
                .size(1000)
                .build();

        user.addAsset(asset);

        /* Act & Assert */
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        Exception exception = assertThrows(InsufficientBalanceException.class, () ->
                orderService.createOrder(email, request)
        );

        assertEquals("Insufficient balance", exception.getMessage());
    }



    @Test
    void should_place_buy_order() {
        /* Arrange */
        var email = faker.internet().emailAddress();
        var request = CreateOrderRequest.builder()
                .asset("USD")
                .price(10)
                .size(10)
                .side(Side.BUY.getSide())
                .build();
        var user = User.builder().build();
        var asset = Asset.builder()
                .assetName("TRY")
                .usableSize(1000)
                .size(1000)
                .build();

        user.addAsset(asset);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        /* Act & Assert */
        orderService.createOrder(email, request);

        assertEquals(900.0, asset.getUsableSize());
        verify(userRepository).save(user);
    }


    @Test
    void should_throw_insufficient_balance_ex_when_no_assets_sell() {
        /* Arrange */
        var email = faker.internet().emailAddress();
        var request = CreateOrderRequest.builder().side(Side.SELL.getSide()).build();
        var user = User.builder().build();

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        /* Act & Assert */
        Exception exception = assertThrows(InsufficientBalanceException.class, () ->
                orderService.createOrder(email, request)
        );

        assertEquals("Insufficient balance", exception.getMessage());
    }

    @Test
    void should_place_sell_order() {
        /* Arrange */
        var email = faker.internet().emailAddress();
        var request = CreateOrderRequest.builder()
                .asset("USD")
                .price(10)
                .size(10)
                .side(Side.SELL.getSide())
                .build();
        var user = User.builder().build();
        var asset = Asset.builder()
                .assetName("USD")
                .usableSize(1000)
                .size(1000)
                .build();

        user.addAsset(asset);
        /* Act & Assert */
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        orderService.createOrder(email, request);

        assertEquals(990.0, asset.getUsableSize());
        verify(userRepository).save(user);
    }

    @Test
    void should_throw_order_not_found_exception() {
        /* Arrange */
        var email = faker.internet().emailAddress();
        var orderId = UUID.randomUUID().toString();
        when(orderRepository.findByUserEmailAndOrderId(email, orderId)).thenReturn(Optional.empty());

        /* Act & Assert */
        Exception exception = assertThrows(OrderNotFoundException.class, () ->
                orderService.deleteOrder(email, orderId)
        );

        assertNotNull(exception);
    }


    @Test
    void should_delete_order() {
        /* Arrange */
        var email = faker.internet().emailAddress();
        var order = Order.builder()
                .id(UUID.randomUUID())
                .status(Status.PENDING)
                .build();

        when(orderRepository.findByUserEmailAndOrderId(email, order.getId().toString())).thenReturn(Optional.of(order));

        /* Act & Assert */
        orderService.deleteOrder(email, order.getId().toString());

        verify(orderRepository).delete(order);
    }

    @Test
    void should_throw_wrong_order_status_ex() {
        /* Arrange */
        var email = faker.internet().emailAddress();
        var order = Order.builder()
                .id(UUID.randomUUID())
                .status(Status.CANCELED)
                .build();
        var orderId = order.getId().toString();
        when(orderRepository.findByUserEmailAndOrderId(email, orderId)).thenReturn(Optional.of(order));

        /* Act & Assert */
        Exception exception = assertThrows(WrongOrderStatusException.class, () ->
                orderService.deleteOrder(email, orderId)
        );

        assertNotNull(exception);
    }
}
