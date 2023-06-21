package com.brigi.inventoryservice.controller;

import com.brigi.inventoryservice.dto.InventoryDto;
import com.brigi.inventoryservice.dto.InventoryRequest;
import com.brigi.inventoryservice.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {
    @Autowired
    private InventoryService inventoryService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void saveInventory(@RequestBody InventoryRequest inventoryRequest) {
        inventoryService.saveInventory(inventoryRequest);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void updateInventories(@RequestBody List<InventoryDto> inventoryDtos) {
        inventoryService.updateInventories(inventoryDtos);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public List<InventoryDto> getBySkuCodes(@RequestParam List<String> skuCode) {
        return inventoryService.getBySkuCodes(skuCode);
    }
}
