package com.example.brokage.data.enums;

public enum TransactionType {
    DEPOSIT("deposit"),
    WITHDRAW("withdraw");

    private final String value;

    TransactionType(String value) {
        this.value = value;
    }

    public static TransactionType fromValue(String value) {
        for (TransactionType transactionType : TransactionType.values()) {
            if (transactionType.getTransactionType().equalsIgnoreCase(value)) {
                return transactionType;
            }
        }
        return null;
    }

    public String getTransactionType() {
        return value;
    }
}
