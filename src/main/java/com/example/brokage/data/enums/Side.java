package com.example.brokage.data.enums;

public enum Side {
    BUY("buy"),
    SELL("sell");

    private final String value;

    Side(String value) {
        this.value = value;
    }

    public static Side fromValue(String value) {
        for (Side side : Side.values()) {
            if (side.getSide().equalsIgnoreCase(value)) {
                return side;
            }
        }
        return null;
    }

    public String getSide() {
        return value;
    }
}
