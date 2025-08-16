package com.pahanaedu.services;

import com.pahanaedu.models.Customer;
import java.util.List;
import java.util.Optional;

public interface ICustomerService {
    List<Customer> getAllCustomers();
    Optional<Customer> getCustomerById(long id);
}