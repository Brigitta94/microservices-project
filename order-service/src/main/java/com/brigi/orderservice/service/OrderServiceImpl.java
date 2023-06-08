package com.brigi.orderservice.service;

import com.brigi.orderservice.dto.OrderLineItemsDto;
import com.brigi.orderservice.dto.OrderRequest;
import com.brigi.orderservice.entity.Order;
import com.brigi.orderservice.entity.OrderLineItems;
import com.brigi.orderservice.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderRepository orderRepository;

    public void placeOrder(final OrderRequest orderRequest) {
        List<OrderLineItems> orderLineItems = orderRequest.orderLineItemsDtos().stream()
                .map(this::mapToEntity)
                .toList();
        Order order = Order.builder()
                .orderNumber(UUID.randomUUID().toString())
                .orderLineItems(orderLineItems)
                .build();
        orderRepository.save(order);
    }

    private OrderLineItems mapToEntity(OrderLineItemsDto orderLineItemsDto) {
        return OrderLineItems.builder().id(orderLineItemsDto.id()
                ).skuCode(orderLineItemsDto.skuCode())
                .price(orderLineItemsDto.price())
                .quantity(orderLineItemsDto.quantity())
                .build();
    }
}
