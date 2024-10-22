package com.example.brokage.data.dto;

import com.example.brokage.data.enums.Side;
import com.example.brokage.data.enums.Status;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto {
    private String assetName;
    private Side side;
    private Status orderStatus;
    private int size;
    private long price;
}
