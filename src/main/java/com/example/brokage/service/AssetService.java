package com.example.brokage.service;

import com.example.brokage.data.response.CustomerListAssetsResponse;

public interface AssetService {
    void deposit(String email, String assetName, long amount);

    void withdraw(String email, String assetName, long amount);

    CustomerListAssetsResponse list(String email);
}
