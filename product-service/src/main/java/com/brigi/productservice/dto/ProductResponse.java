package com.brigi.productservice.dto;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record ProductResponse(
        int id,
        String name,
        String description,
        BigDecimal price
) {
}
