package com.example.brokage.data.response;

import com.example.brokage.data.dto.AssetDto;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerListAssetsResponse {
    private List<AssetDto> assets;
}
