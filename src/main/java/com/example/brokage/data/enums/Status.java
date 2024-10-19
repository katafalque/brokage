package com.example.brokage.data.enums;

public enum Status {
    PENDING("pending"),
    MATCHED("matched"),
    CANCELED("canceled");

    private final String value;

    Status(String value) {
        this.value = value;
    }

    public static Status fromValue(String value) {
        for (Status status : Status.values()) {
            if (status.getStatus().equalsIgnoreCase(value)) {
                return status;
            }
        }
        return null;
    }

    public String getStatus() {
        return value;
    }
}
