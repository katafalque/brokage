package com.example.brokage.exception;

public class AssetNotFoundException extends RuntimeException {
    public AssetNotFoundException(){
        super("Asset not found for given user.");
    }
}
