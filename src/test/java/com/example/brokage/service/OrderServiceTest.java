package com.example.brokage.service;

import com.example.brokage.data.enums.Side;
import com.example.brokage.data.request.CreateOrderRequest;
import com.example.brokage.entity.Asset;
import com.example.brokage.entity.User;
import com.example.brokage.exception.InsufficientBalanceException;
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
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class OrderServiceTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private OrderServiceImpl orderService; // The class that contains createOrder

    private static Faker faker;

    @BeforeEach
    void setUp() {
        faker = new Faker();
    }

    @Test
    void should_throw_user_not_found_ex() {
        var email = faker.internet().emailAddress();
        var request = CreateOrderRequest.builder().build();

        Exception exception = assertThrows(UsernameNotFoundException.class, () ->
                orderService.createOrder(email, request)
        );

        assertEquals("User not found.", exception.getMessage());
    }


    @Test
    void should_throw_insufficient_balance_ex_when_no_assets() {
        var email = faker.internet().emailAddress();
        var request = CreateOrderRequest.builder().build();
        var user = User.builder().build();

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        Exception exception = assertThrows(InsufficientBalanceException.class, () ->
                orderService.createOrder(email, request)
        );

        assertEquals("Insufficient balance", exception.getMessage());
    }


    @Test
    void should_throw_insufficient_balance_ex_when_not_enough_size() {
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

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        Exception exception = assertThrows(InsufficientBalanceException.class, () ->
                orderService.createOrder(email, request)
        );

        assertEquals("Insufficient balance", exception.getMessage());
    }



    @Test
    void should_place_buy_order() {
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

        orderService.createOrder(email, request);

        assertEquals(900.0, asset.getUsableSize());
        verify(userRepository).save(user);
    }


    @Test
    void should_throw_insufficient_balance_ex_when_no_assets_sell() {
        var email = faker.internet().emailAddress();
        var request = CreateOrderRequest.builder().side(Side.SELL.getSide()).build();
        var user = User.builder().build();

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        Exception exception = assertThrows(InsufficientBalanceException.class, () ->
                orderService.createOrder(email, request)
        );

        assertEquals("Insufficient balance", exception.getMessage());
    }

    @Test
    void should_place_sell_order() {
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
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        orderService.createOrder(email, request);

        assertEquals(990.0, asset.getUsableSize());
        verify(userRepository).save(user);
    }

}
