package com.brigi.orderservice.controller;

import com.brigi.orderservice.model.dto.OrderRequest;
import com.brigi.orderservice.service.OrderService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
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
    @CircuitBreaker(name = "inventory", fallbackMethod = "fallBackMethod")
    public String placeOrder(@RequestBody OrderRequest orderRequest) {
        try {
            return orderService.placeOrder(orderRequest);
        } catch (IllegalArgumentException e) {
            return "Product is not found in stock";
        }
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    public boolean deleteOrder(@RequestBody Integer orderID) {
        return orderService.deleteOrder(orderID);
    }

    public String fallBackMethod(OrderRequest orderRequest, RuntimeException runtimeException) {
        return "Oops! Something went wrong!";
    }
}
