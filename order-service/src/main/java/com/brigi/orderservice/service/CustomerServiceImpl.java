package com.brigi.orderservice.service;

import com.brigi.orderservice.model.dto.CustomerDto;
import com.brigi.orderservice.model.dto.CustomerRegistration;
import com.brigi.orderservice.model.entity.Customer;
import com.brigi.orderservice.model.mapper.CustomerMapper;
import com.brigi.orderservice.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerServiceImpl implements CustomerService {
    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CustomerMapper customerMapper;

    @Override
    public void save(CustomerRegistration customerRegistration) {
        customerRepository.save(customerMapper.customerRegistrationToCustomer(customerRegistration));
    }

    @Override
    public List<CustomerDto> getCustomers() {
        return customerRepository.findAll().stream()
                .map(c -> new CustomerDto(c.getId(), c.getFirstName(), c.getLastName(), c.getDateOfBirth(), c.getAddress(), c.getEmail(), c.getPhone())).toList();
    }

    @Override
    public boolean updateCustomer(CustomerDto customerDto) {
        return customerRepository.findById(customerDto.id()).map(c -> {
            customerRepository.save(c);
            return true;
        }).orElse(false);
    }

    @Override
    public boolean deleteCustomer(int customerId) {
        return customerRepository.findById(customerId).map(c -> {
            customerRepository.delete(c);
            return true;
        }).orElse(false);

    }
}
