package com.brigi.orderservice.model.dto;

import java.time.LocalDate;

public record CustomerRegistration(
        String firstName,
        String lastName,
        LocalDate dateOfBirth,
        String address,
        String email,
        String phone
) {
}
