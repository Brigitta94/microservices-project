package com.brigi.orderservice.service;

import com.brigi.orderservice.model.dto.CustomerDto;
import com.brigi.orderservice.model.dto.CustomerRegistration;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CustomerService {
    void save(final CustomerRegistration customerRegistration);

    List<CustomerDto> getCustomers();

    boolean updateCustomer(final CustomerDto customerDto);

    boolean deleteCustomer(final int customerId);
}
