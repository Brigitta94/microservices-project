package com.brigi.orderservice.service;

import com.brigi.orderservice.model.dto.InventoryDto;
import com.brigi.orderservice.model.dto.NotificationResponse;
import com.brigi.orderservice.model.dto.OrderLineItemsDto;
import com.brigi.orderservice.model.dto.OrderRequest;
import com.brigi.orderservice.model.entity.Order;
import com.brigi.orderservice.model.entity.OrderLineItems;
import com.brigi.orderservice.repository.CustomerRepository;
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
    private CustomerRepository customerRepository;
    @Autowired
    private KafkaTemplate<String, NotificationResponse> kafkaTemplate;

    public String placeOrder(final OrderRequest orderRequest) {
        List<OrderLineItems> orderLineItems = orderRequest.orderLineItemsDtos().stream()
                .map(this::mapToEntity)
                .toList();
        System.out.println(orderRequest.customerId());
        Order order = Order.builder()
                .orderNumber(UUID.randomUUID().toString())
                .orderLineItems(orderLineItems)
                .customer(customerRepository.findById(orderRequest.customerId())
                        .orElseThrow(() -> new IllegalArgumentException("Customer not found")))
                .build();
        List<InventoryDto> inventoryDtos = checkIfProductsAreInStock(order);
        if (!inventoryDtos.isEmpty()) {
            orderRepository.save(order);
            kafkaTemplate.send("notificationTopic", new NotificationResponse("Dear " +
                    order.getCustomer().getFirstName() + ", \n Your order with number " +
                    order.getOrderNumber() + " was placed successfully. \n Thank you! ", order.getCustomer().getPhone()));
           // kafkaTemplate.send("topicTwo", "Order id is " + order.getId());
            updateInventoryStock(inventoryDtos);
            return "Order placed successfully";
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
        List<InventoryDto> updatedInventory = order.getOrderLineItems().stream()
                .map(orderLineItem -> Arrays.stream(inventoryDtos)
                        .filter(i -> i.skuCode().equals(orderLineItem.getSkuCode()))
                        .filter(i -> i.quantity() >= orderLineItem.getQuantity())
                        .map(i -> new InventoryDto(i.id(), i.skuCode(), i.quantity() - orderLineItem.getQuantity()))
                        .toList())
                .flatMap(List::stream)
                .toList();
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
