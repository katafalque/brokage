package com.example.brokage.controller;

import com.example.brokage.data.request.CreateOrderRequest;
import com.example.brokage.data.request.CustomerDepositAssetRequest;
import com.example.brokage.service.AssetService;
import com.example.brokage.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/customer")
public class CustomerController {
    private final OrderService orderService;
    private final AssetService assetService;

    @Autowired
    public CustomerController(OrderService orderService, AssetService assetService) {
        this.orderService = orderService;
        this.assetService = assetService;
    }

    @PostMapping("/order/create")
    @PreAuthorize("hasAuthority('CUSTOMER')")
    public ResponseEntity<String> signup(@RequestBody CreateOrderRequest createOrderRequest) {
        orderService.createOrder(createOrderRequest);
        return new ResponseEntity<>("OK", HttpStatus.OK);
    }

    @PostMapping("/asset/deposit")
    @PreAuthorize("hasAuthority('CUSTOMER')")
    public ResponseEntity<String> deposit(@RequestBody CustomerDepositAssetRequest customerDepositAssetRequest) {
        assetService.deposit("alperen@alperen.com", "TRY", 100);
        return new ResponseEntity<>("OK", HttpStatus.OK);
    }

    @PostMapping("/asset/withdraw")
    @PreAuthorize("hasAuthority('CUSTOMER')")
    public ResponseEntity<String> withdraw(@RequestBody CustomerDepositAssetRequest customerDepositAssetRequest) {
        assetService.withdraw("alperen@alperen.com", "TRY", customerDepositAssetRequest.getAmount());
        return new ResponseEntity<>("OK", HttpStatus.OK);
    }
}
