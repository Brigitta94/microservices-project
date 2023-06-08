package com.brigi.orderservice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "order_line_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderLineItems {
    @Id
    @GeneratedValue(generator = "order_line_items_SEQ_generator")
    @SequenceGenerator(name = "order_line_items_SEQ_generator", sequenceName = "order_line_items_SEQ", allocationSize = 1)
    private int id;
    private String skuCode;
    private BigDecimal price;
    private Integer quantity;
    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;
}
