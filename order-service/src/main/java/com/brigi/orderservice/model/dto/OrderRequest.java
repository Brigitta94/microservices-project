package com.brigi.orderservice.model.dto;

import java.util.List;

public record OrderRequest(
        List<OrderLineItemsDto> orderLineItemsDtos,
        int customerId
) {
}
