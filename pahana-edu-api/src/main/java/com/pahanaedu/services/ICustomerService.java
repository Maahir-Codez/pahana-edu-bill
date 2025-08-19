package com.pahanaedu.services;

import com.pahanaedu.exceptions.ValidationException;
import com.pahanaedu.models.Customer;
import java.util.List;
import java.util.Optional;

public interface ICustomerService {
    List<Customer> getAllCustomers();
    Optional<Customer> getCustomerById(long id);
    Customer addCustomer(Customer customer) throws ValidationException;
    void updateCustomer(Customer customer) throws ValidationException;
    void deactivateCustomer(long id);
}