package com.example.brokage.service;

public interface AssetService {
    void deposit(String email, String assetName, long amount);

    void withdraw(String email, String assetName, long amount);
}
