package com.brigi.inventoryservice.dto;

public record InventoryDto(
        int id,
        String skuCode,
        int quantity
) {
}
