package com.brigi.orderservice.service;

import com.brigi.orderservice.dto.InventoryDto;
import com.brigi.orderservice.dto.OrderLineItemsDto;
import com.brigi.orderservice.dto.OrderRequest;
import com.brigi.orderservice.entity.Order;
import com.brigi.orderservice.entity.OrderLineItems;
import com.brigi.orderservice.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private WebClient.Builder webClientBuilder;

    public void placeOrder(final OrderRequest orderRequest) {
        List<OrderLineItems> orderLineItems = orderRequest.orderLineItemsDtos().stream()
                .map(this::mapToEntity)
                .toList();
        Order order = Order.builder()
                .orderNumber(UUID.randomUUID().toString())
                .orderLineItems(orderLineItems)
                .build();

        if (checkIfProductsAreInStock(order)) {
            orderRepository.save(order);
//            updateInventoryStock(orderLineItems);
        } else {
            throw new IllegalArgumentException("Product is not found in stock");
        }
    }

    private OrderLineItems mapToEntity(OrderLineItemsDto orderLineItemsDto) {
        return OrderLineItems.builder().id(orderLineItemsDto.id()
                ).skuCode(orderLineItemsDto.skuCode())
                .price(orderLineItemsDto.price())
                .quantity(orderLineItemsDto.quantity())
                .build();
    }

    private boolean checkIfProductsAreInStock(Order order) {
        List<String> skuCodes = order.getOrderLineItems().stream()
                .map(OrderLineItems::getSkuCode)
                .toList();
        InventoryDto[] inventoryDtos = webClientBuilder.build().get()
                .uri("http://inventory-service/api/inventory",
                        uriBuilder -> uriBuilder.queryParam("skuCode", skuCodes).build())
                .retrieve()
                .bodyToMono(InventoryDto[].class)
                .block();
        Arrays.stream(inventoryDtos).forEach(System.out::println);
        Boolean areProductsInStock = order.getOrderLineItems().stream()
                .map(orderLineItems -> Arrays.stream(inventoryDtos)
                        .filter(i -> i.skuCode().equals(orderLineItems.getSkuCode()))
                        .map(i -> i.quantity() >= orderLineItems.getQuantity())
                        .findFirst()
                        .orElse(false))
                .findFirst()
                .orElse(false);
        return inventoryDtos.length < 0 ? false : areProductsInStock;
    }

//    private void updateInventoryStock(List<OrderLineItems> orderLineItems) {
//        List<InventoryDto> inventoryDtos = orderLineItems.stream()
//                .map(orderLineItems1 ->
//                        new InventoryDto(orderLineItems1.getSkuCode(), orderLineItems1.getQuantity()))
//                .toList();
//        webClient.put()
//                .uri("http://localhost:8082/api/inventory")
//                .body(BodyInserters.fromValue(inventoryDtos))
//                .retrieve()
//                .toBodilessEntity()
//                .block();
//    }
}
