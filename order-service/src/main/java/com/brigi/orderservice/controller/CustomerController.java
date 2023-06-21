package com.brigi.orderservice.controller;

import com.brigi.orderservice.model.dto.CustomerDto;
import com.brigi.orderservice.model.dto.CustomerRegistration;
import com.brigi.orderservice.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("api/customer")
@RestController
public class CustomerController {
    @Autowired
    private CustomerService customerService;

    @PostMapping
    public void saveCustomer(@RequestBody CustomerRegistration customerRegistration) {
        customerService.save(customerRegistration);
    }

    @PutMapping
    public boolean updateCustomer(@RequestBody CustomerDto customerDto) {
        return customerService.updateCustomer(customerDto);
    }

    @GetMapping
    public List<CustomerDto> getCustomers() {
        return customerService.getCustomers();
    }

    @DeleteMapping
    public boolean deleteCustomer(@RequestBody int customerId) {
        return customerService.deleteCustomer(customerId);
    }
}
