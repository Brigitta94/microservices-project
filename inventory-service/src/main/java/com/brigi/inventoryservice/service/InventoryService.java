package com.brigi.inventoryservice.service;

import com.brigi.inventoryservice.dto.InventoryDto;
import com.brigi.inventoryservice.dto.InventoryRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface InventoryService {
    List<InventoryDto> isInStock(final List<String> skuCode);

    void saveInventory(final InventoryRequest inventoryRequest);

    void updateInventories(final List<InventoryDto> inventoryDtos);

    List<InventoryDto> getBySkuCodes(final List<String> skuCodes);
}
