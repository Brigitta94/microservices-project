package com.brigi.orderservice.service;

import com.brigi.orderservice.model.dto.InventoryDto;
import com.brigi.orderservice.model.dto.NotificationResponse;
import com.brigi.orderservice.model.dto.OrderLineItemsDto;
import com.brigi.orderservice.model.dto.OrderRequest;
import com.brigi.orderservice.model.entity.Order;
import com.brigi.orderservice.model.entity.OrderLineItems;
import com.brigi.orderservice.repository.CustomerRepository;
import com.brigi.orderservice.repository.OrderLineItemsRepository;
import com.brigi.orderservice.repository.OrderRepository;
import io.micrometer.observation.annotation.Observed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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
    @Autowired
    private OrderLineItemsRepository orderLineItemsRepository;

    @Transactional
    public String placeOrder(final OrderRequest orderRequest) {
        List<OrderLineItems> orderLineItems = orderRequest.orderLineItemsDtos().stream()
                .map(this::mapToEntity)
                .toList();
        Order order = Order.builder()
                .orderNumber(UUID.randomUUID().toString())
                .orderLineItems(orderLineItems)
                .customer(customerRepository.findById(orderRequest.customerId())
                        .orElseThrow(() -> new IllegalArgumentException("Customer not found")))
                .build();
        List<InventoryDto> inventoryDtos = getOrderProductsInStock(order);
        if (!inventoryDtos.isEmpty()) {
            Order savedOrder = orderRepository.save(order);
            List<OrderLineItems> savedOrderLineItems = orderLineItems.stream()
                    .map(oli -> {
                        oli.setOrder(savedOrder);
                        return oli;
                    }).collect(Collectors.toList());
            orderLineItemsRepository.saveAll(savedOrderLineItems);
            kafkaTemplate.send("notificationTopic", new NotificationResponse("Dear " +
                    order.getCustomer().getFirstName() + ", \n Your order with number " +
                    order.getOrderNumber() + " was placed successfully. \n Thank you! ", order.getCustomer().getPhone()));
            updateInventoryStock(inventoryDtos);
            return "Order placed successfully";
        } else {
            throw new IllegalArgumentException("Product is not found in stock");
        }
    }

    @Override
    @Transactional
    public boolean deleteOrder(Integer orderID) {
        return orderRepository.findById(orderID)
                .map(this::deleteOrderAndItems)
                .orElse(false);
    }

    private OrderLineItems mapToEntity(OrderLineItemsDto orderLineItemsDto) {
        return OrderLineItems.builder().id(orderLineItemsDto.id()
                ).skuCode(orderLineItemsDto.skuCode())
                .price(orderLineItemsDto.price())
                .quantity(orderLineItemsDto.quantity())
                .build();
    }

    private List<InventoryDto> getOrderProductsInStock(Order order) {
        List<InventoryDto> inventoryDtos = getInventoryDtoForOrder(order.getOrderLineItems());
        List<InventoryDto> updatedInventory = order.getOrderLineItems().stream()
                .map(orderLineItem -> inventoryDtos.stream()
                        .filter(i -> i.skuCode().equals(orderLineItem.getSkuCode()))
                        .filter(i -> i.quantity() >= orderLineItem.getQuantity())
                        .map(i -> new InventoryDto(i.id(), i.skuCode(), i.quantity() - orderLineItem.getQuantity()))
                        .toList())
                .flatMap(List::stream)
                .toList();
        return updatedInventory.size() == inventoryDtos.size() ? updatedInventory : List.of();
    }

    private List<InventoryDto> getInventoryDtoForOrder(List<OrderLineItems> orderLineItems) {
        List<String> skuCodes = orderLineItems.stream()
                .map(OrderLineItems::getSkuCode)
                .toList();
        InventoryDto[] inventoryDtos = webClientBuilder.build().get()
                .uri("http://inventory-service/api/inventory",
                        uriBuilder -> uriBuilder.queryParam("skuCode", skuCodes).build())
                .retrieve()
                .bodyToMono(InventoryDto[].class)
                .block();

        return Arrays.stream(inventoryDtos).toList();
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

    @Transactional
    private boolean deleteOrderAndItems(Order order) {
        List<OrderLineItems> orderLineItems = orderLineItemsRepository.findByOrderId(order.getId());
        order.setCanceled(true);
        orderRepository.save(order);
        List<InventoryDto> inventoryDtos = returnCanceledOrderedItemsInStock(order.getOrderLineItems());
        updateInventoryStock(inventoryDtos);
        orderLineItemsRepository.deleteAll(orderLineItems);
        return true;
    }

    private List<InventoryDto> returnCanceledOrderedItemsInStock(List<OrderLineItems> orderLineItems) {
        List<InventoryDto> inventoryDtos = getInventoryDtoForOrder(orderLineItems);
        List<InventoryDto> updatedInventory = orderLineItems.stream()
                .map(orderLineItem -> inventoryDtos.stream()
                        .filter(i -> i.skuCode().equals(orderLineItem.getSkuCode()))
                        .map(i -> new InventoryDto(i.id(), i.skuCode(), i.quantity() + orderLineItem.getQuantity()))
                        .toList())
                .flatMap(List::stream)
                .toList();
        return updatedInventory.size() == inventoryDtos.size() ? updatedInventory : List.of();
    }

}
