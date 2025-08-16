package com.pahanaedu.dao;

import com.pahanaedu.models.Customer;
import java.util.List;
import java.util.Optional;

public interface ICustomerDAO {
    List<Customer> findAll();
    Optional<Customer> findById(long id);
}