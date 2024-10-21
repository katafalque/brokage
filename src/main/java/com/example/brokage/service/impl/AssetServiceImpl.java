package com.example.brokage.service.impl;

import com.example.brokage.entity.Asset;
import com.example.brokage.entity.User;
import com.example.brokage.exception.AssetNotFoundException;
import com.example.brokage.exception.InsufficientBalanceException;
import com.example.brokage.repository.AssetRepository;
import com.example.brokage.repository.UserRepository;
import com.example.brokage.service.AssetService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AssetServiceImpl implements AssetService {
    private final AssetRepository assetRepository;
    private final UserRepository userRepository;

    public AssetServiceImpl(AssetRepository assetRepository, UserRepository userRepository) {
        this.assetRepository = assetRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void deposit(String email, String assetName, long amount) {
        Optional<Asset> optionalAsset = assetRepository.findByUserEmailAndAssetName(email, assetName);

        if (optionalAsset.isPresent()){
            handleNonFirstTimeDeposit(optionalAsset.get(), amount);
        } else {
            handleFirstTimeDeposit(email, assetName, amount);
        }
    }

    @Override
    public void withdraw(String email, String assetName, long amount) {
        Asset asset = this.assetRepository.findByUserEmailAndAssetName(email, assetName)
                .orElseThrow(AssetNotFoundException::new);

        if (asset.getUsableSize() < amount || asset.getSize() < amount) throw new InsufficientBalanceException();

        long oldSize = asset.getSize();
        long oldUsableSize = asset.getSize();
        asset.setSize(oldSize - amount);
        asset.setUsableSize(oldUsableSize - amount);
        this.assetRepository.save(asset);
    }

    private void handleNonFirstTimeDeposit(Asset asset, long amount){
        long oldSize = asset.getSize();
        long oldUsableSize = asset.getUsableSize();
        asset.setSize(oldSize + amount);
        asset.setUsableSize(oldUsableSize + amount);
        assetRepository.save(asset);
    }

    private void handleFirstTimeDeposit(String email, String assetName, long amount){
        User user = this.userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found."));

        Asset asset = Asset.builder()
                .assetName(assetName)
                .size(amount)
                .usableSize(amount)
                .build();

        user.addAsset(asset);
        this.userRepository.save(user);
    }
}
