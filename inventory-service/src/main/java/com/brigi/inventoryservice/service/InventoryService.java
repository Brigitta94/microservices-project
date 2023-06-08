package com.brigi.inventoryservice.service;

import com.brigi.inventoryservice.dto.InventoryDto;
import org.springframework.stereotype.Service;

@Service
public interface InventoryService {
    boolean isInStock(final String skuCode);

    void saveInventory(InventoryDto inventoryDto);
}
