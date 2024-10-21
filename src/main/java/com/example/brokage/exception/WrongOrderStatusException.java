package com.example.brokage.exception;

public class WrongOrderStatusException extends RuntimeException {
    public WrongOrderStatusException(){
        super("Only pending orders can be deleted.");
    }
}
