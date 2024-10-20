package com.example.brokage.service.impl;

import com.example.brokage.data.enums.Role;
import com.example.brokage.data.request.LoginRequest;
import com.example.brokage.data.request.SignupRequest;
import com.example.brokage.entity.User;
import com.example.brokage.entity.UserRole;
import com.example.brokage.repository.UserRepository;
import com.example.brokage.service.AuthService;
import com.example.brokage.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthServiceImpl(AuthenticationManager authenticationManager, UserRepository userRepository, JwtService jwtService, PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public String login(LoginRequest request) {
        this.authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Invalid Credentials."));

        return jwtService.generateToken(user);
    }

    @Override
    public void register(SignupRequest request) {
        UserRole userRole = UserRole.builder()
                .role(Role.fromValue(request.getRole()))
                .build();

        User user = User.builder()
                .email(request.getEmail())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .password(this.passwordEncoder.encode(request.getPassword()))
                .build();

        user.addRole(userRole);
        this.userRepository.save(user);
    }
}
