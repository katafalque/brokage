package com.example.brokage.exception;

import org.springframework.security.core.AuthenticationException;

public class AuthTokenMissingException extends AuthenticationException {
    public AuthTokenMissingException(String msg) {
        super(msg);
    }
}
