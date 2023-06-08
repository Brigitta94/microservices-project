package com.brigi.inventoryservice.dto;

public record InventoryDto(
        String skuCode,
        int quantity
) {
}
