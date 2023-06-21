package com.brigi.inventoryservice.dto;

public record InventoryRequest(
        String skuCode,
        int quantity
) {
}
