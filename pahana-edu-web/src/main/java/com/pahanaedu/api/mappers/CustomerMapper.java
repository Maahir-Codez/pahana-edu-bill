package com.pahanaedu.api.mappers;

import com.pahanaedu.api.dto.CustomerDTO;
import com.pahanaedu.models.Customer;
import java.util.List;
import java.util.stream.Collectors;

public final class CustomerMapper {
    private CustomerMapper() {}

    public static CustomerDTO toDTO(Customer customer) {
        if (customer == null) return null;
        CustomerDTO dto = new CustomerDTO();
        dto.setId(customer.getId());
        dto.setAccountNumber(customer.getAccountNumber());
        dto.setFullName(customer.getFullName());
        dto.setAddress(customer.getAddress());
        dto.setCity(customer.getCity());
        dto.setPostalCode(customer.getPostalCode());
        dto.setPhoneNumber(customer.getPhoneNumber());
        dto.setEmail(customer.getEmail());
        return dto;
    }

    public static List<CustomerDTO> toDTOList(List<Customer> customers) {
        return customers.stream()
                .map(CustomerMapper::toDTO)
                .collect(Collectors.toList());
    }

    public static Customer toModel(CustomerDTO dto) {
        if (dto == null) return null;
        Customer customer = new Customer();
        customer.setId(dto.getId());
        customer.setAccountNumber(dto.getAccountNumber());
        customer.setFullName(dto.getFullName());
        customer.setAddress(dto.getAddress());
        customer.setCity(dto.getCity());
        customer.setPostalCode(dto.getPostalCode());
        customer.setPhoneNumber(dto.getPhoneNumber());
        customer.setEmail(dto.getEmail());
        return customer;
    }
}