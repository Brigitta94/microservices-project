package com.brigi.orderservice.dto;

import java.util.List;

public record OrderRequest(
        List<OrderLineItemsDto> orderLineItemsDtos
) {
}
