package com.brigi.orderservice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {
    @Id
    @GeneratedValue(generator = "orders_SEQ_generator")
    @SequenceGenerator(name = "orders_SEQ_generator", sequenceName = "orders_SEQ", allocationSize = 1)
    private int id;
    private String orderNumber;
    @OneToMany(mappedBy = "order")
    private List<OrderLineItems> orderLineItems;
}
