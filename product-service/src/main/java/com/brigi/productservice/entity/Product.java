package com.brigi.productservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(generator = "products_SEQ_generator")
    @SequenceGenerator(name = "products_SEQ_generator", sequenceName = "products_SEQ", allocationSize = 1)
    private int id;
    private String name;
    private String description;
    private BigDecimal price;
}
