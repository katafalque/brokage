package com.example.brokage.mapper;

import com.example.brokage.data.dto.AssetDto;
import com.example.brokage.data.response.CustomerListAssetsResponse;
import com.example.brokage.entity.Asset;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class CustomerListAssetResponseMapperTest {
    private CustomerListAssetResponseMapper mapper;
    private static Faker faker;

    @BeforeEach
    public void setUp() {
        faker = new Faker();
        mapper = new CustomerListAssetResponseMapperImpl();
    }

    @Test
    void should_map_to_asset_dto() {
        /* Arrange */
        var asset = Asset.builder()
                .usableSize(faker.random().nextInt(10, 100).longValue())
                .assetName(faker.stock().nsdqSymbol())
                .size(faker.random().nextInt(10, 100).longValue())
                .build();

        /* Act */
        AssetDto assetDto = mapper.assetToAssetDto(asset);

        /* Assert */
        assertEquals(asset.getAssetName(), assetDto.getAssetName());
        assertEquals(asset.getSize(), assetDto.getSize());
        assertEquals(asset.getUsableSize(), assetDto.getUsableSize());
    }

    @Test
    void should_map_to_customer_assets_response() {
        /* Arrange */
        Set<Asset> assets = new HashSet<>();
        var asset1 = Asset.builder()
                .usableSize(faker.random().nextInt(10, 100).longValue())
                .assetName(faker.stock().nsdqSymbol())
                .size(faker.random().nextInt(10, 100).longValue())
                .build();

        var asset2 = Asset.builder()
                .usableSize(faker.random().nextInt(10, 100).longValue())
                .assetName(faker.stock().nsdqSymbol())
                .size(faker.random().nextInt(10, 100).longValue())
                .build();
        assets.add(asset1);
        assets.add(asset2);

        /* Act */
        CustomerListAssetsResponse response = mapper.mapToCustomerListAssetsResponse(assets);

        /* Assert */
        assertEquals(assets.size(), response.getAssets().size());
        assertEquals(asset1.getAssetName(), response.getAssets().get(0).getAssetName());
        assertEquals(asset1.getSize(), response.getAssets().get(0).getSize());
        assertEquals(asset1.getUsableSize(), response.getAssets().get(0).getUsableSize());
        assertEquals(asset2.getAssetName(), response.getAssets().get(1).getAssetName());
        assertEquals(asset2.getSize(), response.getAssets().get(1).getSize());
        assertEquals(asset2.getUsableSize(), response.getAssets().get(1).getUsableSize());
    }


    @Test
    void should_map_empty_set() {
        /* Arrange */
        Set<Asset> assets = new HashSet<>();

        /* Act */
        CustomerListAssetsResponse response = mapper.mapToCustomerListAssetsResponse(assets);

        /* Assert */
        assertEquals(0, response.getAssets().size());
    }
}
