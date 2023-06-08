package com.brigi.inventoryservice.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "inventories")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Inventory {
    @Id
    @GeneratedValue(generator = "inventories_SEQ_generator")
    @SequenceGenerator(name = "inventories_SEQ_generator", sequenceName = "inventories_SEQ", allocationSize = 1)
    private int id;
    private String skuCode;
    private int quantity;
}
