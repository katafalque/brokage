package com.example.brokage.service;

import com.example.brokage.data.request.LoginRequest;
import com.example.brokage.data.request.SignupRequest;

public interface AuthService {
    String login(LoginRequest request);

    void register(SignupRequest request);
}
