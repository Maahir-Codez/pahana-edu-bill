package com.pahanaedu.dao;

import com.pahanaedu.models.Customer;
import java.util.List;
import java.util.Optional;

public interface ICustomerDAO {
    List<Customer> findAll();
    Optional<Customer> findById(long id);
    Customer create(Customer customer);
    void update(Customer customer);
    boolean existsByAccountNumber(String accountNumber, Long customerIdToIgnore);
    boolean existsByEmail(String email, Long customerIdToIgnore);
    void deactivate(long id);
}