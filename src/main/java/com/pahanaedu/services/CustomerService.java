package com.pahanaedu.services;

import com.pahanaedu.dao.CustomerDAO;
import com.pahanaedu.dao.ICustomerDAO;
import com.pahanaedu.models.Customer;

import java.util.List;
import java.util.Optional;

public class CustomerService implements ICustomerService {

    private final ICustomerDAO customerDAO;

    public CustomerService() {
        this.customerDAO = new CustomerDAO();
    }

    @Override
    public List<Customer> getAllCustomers() {
        return customerDAO.findAll();
    }

    @Override
    public Optional<Customer> getCustomerById(long id) {
        return customerDAO.findById(id);
    }
}