package com.brigi.orderservice.service;

import com.brigi.orderservice.model.dto.InventoryDto;
import com.brigi.orderservice.model.dto.NotificationResponse;
import com.brigi.orderservice.model.dto.OrderLineItemsDto;
import com.brigi.orderservice.model.dto.OrderRequest;
import com.brigi.orderservice.model.entity.Order;
import com.brigi.orderservice.model.entity.OrderLineItems;
import com.brigi.orderservice.repository.OrderRepository;
import io.micrometer.observation.annotation.Observed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Observed(name = "orderService")
@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private WebClient.Builder webClientBuilder;

    @Autowired
    private KafkaTemplate<String, NotificationResponse> kafkaTemplate;

    public String placeOrder(final OrderRequest orderRequest) {
        List<OrderLineItems> orderLineItems = orderRequest.orderLineItemsDtos().stream()
                .map(this::mapToEntity)
                .toList();
        Order order = Order.builder()
                .orderNumber(UUID.randomUUID().toString())
                .orderLineItems(orderLineItems)
                .build();
        List<InventoryDto> inventoryDtos = checkIfProductsAreInStock(order);
        if (!inventoryDtos.isEmpty()) {
            orderRepository.save(order);
            kafkaTemplate.send("notificationTopic", new NotificationResponse("Order number is " + order.getOrderNumber()));
            kafkaTemplate.send("topicTwo", new NotificationResponse("Order id is " + order.getId()));
            updateInventoryStock(inventoryDtos);
            return "Order placed succesfully";
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

    private List<InventoryDto> checkIfProductsAreInStock(Order order) {
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
        // skuCodes.stream().filter(s -> )
        List<InventoryDto> updatedInventory = order.getOrderLineItems().stream()
                .map(orderLineItem -> Arrays.stream(inventoryDtos)
                        .filter(i -> i.skuCode().equals(orderLineItem.getSkuCode()))
                        .filter(i -> i.quantity() >= orderLineItem.getQuantity())
                        .map(i -> new InventoryDto(i.id(), i.skuCode(), i.quantity() - orderLineItem.getQuantity()))
                        .toList())
                .flatMap(List::stream)
                .toList();
        (updatedInventory).stream().forEach(System.out::println);
        return updatedInventory.size() == skuCodes.size() ? updatedInventory : List.of();
    }

    private void updateInventoryStock(List<InventoryDto> inventoryDtos) {
        webClientBuilder.build()
                .put()
                .uri("http://inventory-service/api/inventory")
                .body(BodyInserters.fromValue(inventoryDtos))
                .retrieve()
                .toBodilessEntity()
                .block();
    }
}
