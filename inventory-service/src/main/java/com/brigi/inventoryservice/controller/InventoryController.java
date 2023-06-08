package com.brigi.inventoryservice.controller;

import com.brigi.inventoryservice.dto.InventoryDto;
import com.brigi.inventoryservice.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {
    @Autowired
    private InventoryService inventoryService;

    @GetMapping ("/sku-code")
    @ResponseStatus(HttpStatus.CREATED)
    public boolean isInStock(@PathVariable("sku-code") String skuCode) {
       return inventoryService.isInStock(skuCode);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void saveInventory(@RequestBody InventoryDto inventoryDto) {
        inventoryService.saveInventory(inventoryDto);
    }
}
