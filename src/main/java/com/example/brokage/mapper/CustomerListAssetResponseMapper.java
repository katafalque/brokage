package com.example.brokage.mapper;

import com.example.brokage.data.dto.AssetDto;
import com.example.brokage.data.response.CustomerListAssetsResponse;
import com.example.brokage.entity.Asset;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface CustomerListAssetResponseMapper {
    @Mapping(source = "assetName", target = "assetName")
    @Mapping(source = "size", target = "size")
    @Mapping(source = "usableSize", target = "usableSize")
    AssetDto assetToAssetDto(Asset asset);

    default CustomerListAssetsResponse mapToCustomerListAssetsResponse(Set<Asset> assets) {
        List<AssetDto> assetDtos = assets.stream()
                .map(this::assetToAssetDto)
                .collect(Collectors.toList());
        return CustomerListAssetsResponse.builder()
                .assets(assetDtos)
                .build();
    }
}
