package com.brigi.orderservice.controller;

import com.brigi.orderservice.dto.OrderRequest;
import com.brigi.orderservice.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/order")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String placeOrder(@RequestBody OrderRequest orderRequest) {
        System.out.println(orderRequest);
        orderService.placeOrder(orderRequest);
        return "Order placed succesfully";
    }
}
