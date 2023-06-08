package com.brigi.orderservice.dto;

public record InventoryDto(
        String skuCode,
        int quantity
) {
}
