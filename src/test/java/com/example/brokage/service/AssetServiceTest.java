package com.example.brokage.service;

import com.example.brokage.entity.Asset;
import com.example.brokage.entity.User;
import com.example.brokage.exception.AssetNotFoundException;
import com.example.brokage.exception.InsufficientBalanceException;
import com.example.brokage.repository.AssetRepository;
import com.example.brokage.repository.UserRepository;
import com.example.brokage.service.impl.AssetServiceImpl;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class AssetServiceImplTest {

    @Mock
    private AssetRepository assetRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AssetServiceImpl assetService;

    private static Faker faker;

    @BeforeEach
    void setup(){
        faker = new Faker();
    }


    @Test
    void should_deposit_first_time_deposit() {
        /* Arrange */
        var email = faker.internet().emailAddress();
        var assetName = faker.stock().nsdqSymbol();
        var amount = faker.random().nextLong();

        var user = User.builder()
                .email(email)
                .build();

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(assetRepository.findByUserEmailAndAssetName(email, assetName)).thenReturn(Optional.empty());

        /* Act */
        assetService.deposit(email, assetName, amount);

        /* Assert */
        verify(userRepository).findByEmail(email);
        verify(userRepository).save(any(User.class));
    }


    @Test
    void should_deposit_non_first_time_deposit() {
        /* Arrange */
        var email = faker.internet().emailAddress();
        var assetName = faker.stock().nsdqSymbol();
        var amount = faker.random().nextLong();

        var user = User.builder()
                .email(email)
                .build();

        var asset = Asset.builder()
                .user(user)
                .assetName(assetName)
                .usableSize(faker.random().nextLong())
                .size(faker.random().nextLong())
                .build();

        when(assetRepository.findByUserEmailAndAssetName(email, assetName)).thenReturn(Optional.of(asset));

        /* Act */
        assetService.deposit(email, assetName, amount);

        /* Assert */
        verify(assetRepository).save(asset);
    }

    @Test
    void should_withdraw_successfully() {
        /* Arrange */
        var assetName = faker.stock().nsdqSymbol();
        var email = faker.internet().emailAddress();
        var amount = faker.random().nextInt(0, 100);

        var asset = Asset.builder()
                .size(faker.random().nextInt(amount, amount * 10).longValue())
                .usableSize(faker.random().nextInt(amount, amount * 10).longValue())
                .build();

        when(assetRepository.findByUserEmailAndAssetName(email, assetName)).thenReturn(Optional.of(asset));

        /* Act */
        assetService.withdraw(email, assetName, amount.longValue());

        /* Assert */
        verify(assetRepository).save(asset);
    }


    @Test
    void should_throw_insufficient_balance_ex() {
        /* Arrange */
        var assetName = faker.stock().nsdqSymbol();
        var email = faker.internet().emailAddress();
        var amount = faker.random().nextInt(1000, 10000);

        var asset = Asset.builder()
                .size(faker.random().nextInt(0, 999).longValue())
                .usableSize(faker.random().nextInt(0, 999).longValue())
                .build();

        when(assetRepository.findByUserEmailAndAssetName(email, assetName)).thenReturn(Optional.of(asset));

        /* Act */
        Exception exception = assertThrows(InsufficientBalanceException.class, () -> {
            assetService.withdraw(email, assetName, amount);
        });

        /* Assert */
        assertEquals("Insufficient balance", exception.getMessage());
        verify(assetRepository, never()).save(asset);
    }

    @Test
    void should_throw_asset_not_found_ex() {
        /* Arrange */
        var email = faker.internet().emailAddress();
        var assetName = faker.stock().nsdqSymbol();
        var amount = faker.random().nextInt(10, 100).longValue();

        when(assetRepository.findByUserEmailAndAssetName(email, assetName)).thenReturn(Optional.empty());

        /* Act */
        Exception exception = assertThrows(AssetNotFoundException.class, () -> {
            assetService.withdraw(email, assetName, amount);
        });

        /* Assert */
        assertNotNull(exception);
        verify(assetRepository, never()).save(any());
    }
}