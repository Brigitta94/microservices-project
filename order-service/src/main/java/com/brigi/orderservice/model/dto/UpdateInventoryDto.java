package com.brigi.orderservice.model.dto;

public record UpdateInventoryDto(
        int id,
        String skuCode,
        int quantity
) {
}
