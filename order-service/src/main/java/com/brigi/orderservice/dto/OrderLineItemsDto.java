package com.brigi.orderservice.dto;

import java.math.BigDecimal;

public record OrderLineItemsDto(
        int id,
        String skuCode,
        BigDecimal price,
        Integer quantity
) {
}
