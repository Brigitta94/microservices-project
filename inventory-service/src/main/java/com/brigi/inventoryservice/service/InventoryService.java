package com.brigi.inventoryservice.service;

import com.brigi.inventoryservice.dto.InventoryDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface InventoryService {
    List<InventoryDto> isInStock(final List<String> skuCode);

    void saveInventory(final InventoryDto inventoryDto);

    void updateInventories(final List<InventoryDto> inventoryDtos);
}
