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
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    private final OrderService orderService;
    private final AssetService assetService;

    @Autowired
    public AdminController(OrderService orderService, AssetService assetService) {
        this.orderService = orderService;
        this.assetService = assetService;
    }

    @PostMapping("/asset/deposit")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> deposit(@RequestBody CustomerDepositAssetRequest customerDepositAssetRequest,
                                          @RequestParam String customerEmail) {
        assetService.deposit(
                customerEmail,
                "TRY",
                customerDepositAssetRequest.getAmount()
        );
        return new ResponseEntity<>("OK", HttpStatus.OK);
    }

    @PostMapping("/asset/withdraw")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> withdraw(@RequestBody CustomerWithdrawAssetRequest customerWithdrawAssetRequest,
                                           @RequestParam String customerEmail) {
        assetService.withdraw(
                customerEmail,
                "TRY",
                customerWithdrawAssetRequest.getAmount()
        );
        return new ResponseEntity<>("OK", HttpStatus.OK);
    }

    @GetMapping("/asset/list")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<CustomerListAssetsResponse> listAssets(@RequestParam String customerEmail) {
        CustomerListAssetsResponse response = assetService.list(
                customerEmail
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/order/create")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Object> createOrder(@RequestBody CreateOrderRequest createOrderRequest,
                                              @RequestParam String customerEmail) {
        orderService.createOrder(
                customerEmail,
                createOrderRequest
        );
        return new ResponseEntity<>("OK", HttpStatus.OK);
    }

    @PostMapping("/order/delete")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Object> deleteOrder(@RequestParam String orderId,
                                              @RequestParam String customerEmail) {
        orderService.deleteOrder(
                customerEmail,
                orderId
        );
        return new ResponseEntity<>("OK", HttpStatus.OK);
    }

    @PostMapping("/order/list")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Object> listOrders(@RequestBody ListOrdersRequest listOrdersRequest,
                                             @RequestParam String customerEmail) {
        var resp = orderService.getByFilter(
                customerEmail,
                listOrdersRequest
        );
        return new ResponseEntity<>(resp, HttpStatus.OK);
    }
}
