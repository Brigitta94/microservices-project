package com.brigi.orderservice.service;

import com.brigi.orderservice.model.dto.OrderRequest;
import org.springframework.stereotype.Service;

@Service
public interface OrderService {
    String placeOrder(final OrderRequest orderRequest);
}
