package com.brigi.orderservice.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "customers")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class Customer {
    @Id
    @GeneratedValue(generator = "orders_SEQ_generator")
    @SequenceGenerator(name = "orders_SEQ_generator", sequenceName = "orders_SEQ", allocationSize = 1)
    private int id;
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private String address;
    private String email;
    private String phone;
}
