package com.example.brokage.data.request;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDepositAssetRequest {
    private long amount;
}
