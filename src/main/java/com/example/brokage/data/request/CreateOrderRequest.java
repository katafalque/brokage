package com.example.brokage.data.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CreateOrderRequest {
    @JsonProperty("customer_id")
    private String customerId;
    private String asset;
    private String side;
    private int size;
    private long price;
}
