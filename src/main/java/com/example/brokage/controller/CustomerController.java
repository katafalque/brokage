package com.example.brokage.controller;

import com.example.brokage.data.request.CreateOrderRequest;
import com.example.brokage.data.request.CustomerDepositAssetRequest;
import com.example.brokage.data.request.CustomerWithdrawAssetRequest;
import com.example.brokage.data.request.ListOrdersRequest;
import com.example.brokage.data.response.CustomerListAssetsResponse;
import com.example.brokage.service.AssetService;
import com.example.brokage.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/asset/deposit")
    @PreAuthorize("hasAuthority('CUSTOMER')")
    public ResponseEntity<String> deposit(@RequestBody CustomerDepositAssetRequest customerDepositAssetRequest) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        assetService.deposit(
                userDetails.getUsername(),
                "TRY",
                customerDepositAssetRequest.getAmount()
        );
        return new ResponseEntity<>("OK", HttpStatus.OK);
    }

    @PostMapping("/asset/withdraw")
    @PreAuthorize("hasAuthority('CUSTOMER')")
    public ResponseEntity<String> withdraw(@RequestBody CustomerWithdrawAssetRequest customerWithdrawAssetRequest) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        assetService.withdraw(
                userDetails.getUsername(),
                "TRY",
                customerWithdrawAssetRequest.getAmount()
        );
        return new ResponseEntity<>("OK", HttpStatus.OK);
    }

    @GetMapping("/asset/list")
    @PreAuthorize("hasAuthority('CUSTOMER')")
    public ResponseEntity<CustomerListAssetsResponse> listAssets() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        CustomerListAssetsResponse response = assetService.list(
                userDetails.getUsername()
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/order/create")
    @PreAuthorize("hasAuthority('CUSTOMER')")
    public ResponseEntity<Object> createOrder(@RequestBody CreateOrderRequest createOrderRequest) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        orderService.createOrder(
                userDetails.getUsername(),
                createOrderRequest
        );
        return new ResponseEntity<>("OK", HttpStatus.OK);
    }

    @PostMapping("/order/delete")
    @PreAuthorize("hasAuthority('CUSTOMER')")
    public ResponseEntity<Object> deleteOrder(@RequestParam String orderId) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        orderService.deleteOrder(
                userDetails.getUsername(),
                orderId
        );
        return new ResponseEntity<>("OK", HttpStatus.OK);
    }

    @PostMapping("/order/list")
    @PreAuthorize("hasAuthority('CUSTOMER')")
    public ResponseEntity<Object> listOrders(@RequestBody ListOrdersRequest listOrdersRequest) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var resp = orderService.getByFilter(
                userDetails.getUsername(),
                listOrdersRequest
        );
        return new ResponseEntity<>(resp, HttpStatus.OK);
    }
}
