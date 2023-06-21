package com.brigi.orderservice.model.mapper;


import com.brigi.orderservice.model.dto.CustomerDto;
import com.brigi.orderservice.model.dto.CustomerRegistration;
import com.brigi.orderservice.model.entity.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CustomerMapper {

    CustomerDto toCustomerDto(Customer customer);

    Customer toCustomer(CustomerDto customerDto);

    Customer customerRegistrationToCustomer(CustomerRegistration customerRegistration);
}
