package com.brigi.inventoryservice.service;

import com.brigi.inventoryservice.dto.InventoryDto;
import com.brigi.inventoryservice.dto.InventoryRequest;
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
                .map(inventory -> new InventoryDto(inventory.getId(),inventory.getSkuCode(), inventory.getQuantity()))
                .toList();
    }

    @Override
    public void saveInventory(InventoryRequest inventoryRequest) {
        Inventory inventory = Inventory.builder()
                .quantity(inventoryRequest.quantity())
                .skuCode(inventoryRequest.skuCode())
                .build();
        inventoryRepository.save(inventory);
    }

    @Override
    public void updateInventories(List<InventoryDto> inventoryDtos) {
        List<Inventory> inventories= inventoryDtos.stream()
                .map(inventoryDto -> Inventory.builder()
                        .id(inventoryDto.id())
                        .skuCode(inventoryDto.skuCode())
                        .quantity(inventoryDto.quantity())
                        .build()
                ).toList();
        inventoryRepository.saveAll(inventories);
    }

    @Override
    public List<InventoryDto> getBySkuCodes(List<String> skuCodes) {
        return inventoryRepository.findBySkuCodeIn(skuCodes).stream()
                .map(i -> new InventoryDto(i.getId(), i.getSkuCode(), i.getQuantity()))
                .toList();
    }
}
