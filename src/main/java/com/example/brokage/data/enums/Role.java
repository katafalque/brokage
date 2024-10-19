package com.example.brokage.data.enums;

public enum Role {
    CUSTOMER("customer"),
    ADMIN("admin");

    private final String value;

    Role(String value) {
        this.value = value;
    }

    public static Role fromValue(String value) {
        for (Role role : Role.values()) {
            if (role.getRole().equalsIgnoreCase(value)) {
                return role;
            }
        }
        return null;
    }

    public String getRole() {
        return value;
    }
}
