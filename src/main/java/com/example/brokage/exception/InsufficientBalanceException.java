package com.example.brokage.exception;

public class InsufficientBalanceException extends RuntimeException{
    public InsufficientBalanceException(){
        super("Insufficient balance");
    }
}
