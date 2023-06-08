package com.brigi.inventoryservice.repository;

import com.brigi.inventoryservice.entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Integer> {
    List<Inventory> findBySkuCodeIn(final List<String> skuCode);

    Optional<Inventory> findBySkuCode(final String skuCode);
}
