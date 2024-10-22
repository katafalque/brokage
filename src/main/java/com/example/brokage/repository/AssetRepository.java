package com.example.brokage.repository;

import com.example.brokage.entity.Asset;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AssetRepository extends JpaRepository<Asset, UUID> {

    @Query("SELECT a FROM Asset a WHERE a.user.email = :email AND a.assetName = :assetName")
    Optional<Asset> findByUserEmailAndAssetName(@Param("email") String email, @Param("assetName") String assetName);
}
