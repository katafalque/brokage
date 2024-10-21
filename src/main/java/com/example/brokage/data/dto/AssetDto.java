package com.example.brokage.data.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AssetDto {
    private String assetName;
    private long size;
    private long usableSize;
}
