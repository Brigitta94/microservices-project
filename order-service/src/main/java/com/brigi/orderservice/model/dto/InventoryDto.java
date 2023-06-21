package com.brigi.orderservice.model.dto;

public record InventoryDto(
        int id,
        String skuCode,
        int quantity
) {
}
