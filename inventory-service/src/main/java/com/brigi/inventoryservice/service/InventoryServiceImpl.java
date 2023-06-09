package com.brigi.inventoryservice.service;

import com.brigi.inventoryservice.dto.InventoryDto;
import com.brigi.inventoryservice.entity.Inventory;
import com.brigi.inventoryservice.repository.InventoryRepository;
import io.micrometer.observation.annotation.Observed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Observed(name = "inventory-service")
@Service
public class InventoryServiceImpl implements InventoryService {
    @Autowired
    private InventoryRepository inventoryRepository;

    @Transactional(readOnly = true)
    @Override
    public List<InventoryDto> isInStock(final List<String> skuCode) {
        return inventoryRepository.findBySkuCodeIn(skuCode).stream()
                .map(inventory -> new InventoryDto(inventory.getSkuCode(), inventory.getQuantity()))
                .toList();
    }

    @Override
    public void saveInventory(InventoryDto inventoryDto) {
        Inventory inventory = Inventory.builder()
                .quantity(inventoryDto.quantity())
                .skuCode(inventoryDto.skuCode())
                .build();
        inventoryRepository.save(inventory);
    }

    @Override
    public void updateInventories(List<InventoryDto> inventoryDtos) {
        List<Inventory> updateInventories = inventoryDtos.stream()
                .map(inventoryDto -> inventoryRepository.findBySkuCode(inventoryDto.skuCode())
                        .map(inventory -> Inventory.builder().id(inventory.getId())
                                .skuCode(inventory.getSkuCode())
                                .quantity(inventory.getQuantity() - inventoryDto.quantity()).build())
                        .orElseThrow(() -> new IllegalArgumentException())
                ).toList();
        inventoryRepository.saveAll(updateInventories);
    }
}
