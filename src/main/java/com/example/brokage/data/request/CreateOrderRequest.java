package com.example.brokage.data.request;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrderRequest {
    private String asset;
    private String side;
    private int size;
    private long price;
}
