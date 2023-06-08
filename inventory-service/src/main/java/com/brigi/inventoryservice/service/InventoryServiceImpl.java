package com.brigi.inventoryservice.service;

import com.brigi.inventoryservice.dto.InventoryDto;
import com.brigi.inventoryservice.entity.Inventory;
import com.brigi.inventoryservice.repository.InventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class InventoryServiceImpl implements InventoryService {
    @Autowired
    private InventoryRepository inventoryRepository;

    //@Transactional(readOnly = true)
    @Override
    public boolean isInStock(final String skuCode) {
        return inventoryRepository.findBySkuCode(skuCode).isPresent();
    }

    @Override
    public void saveInventory(InventoryDto inventoryDto) {
        Inventory inventory = Inventory.builder()
                .quantity(inventoryDto.quantity())
                .skuCode(inventoryDto.skuCode())
                .build();
        inventoryRepository.save(inventory);
    }
}
