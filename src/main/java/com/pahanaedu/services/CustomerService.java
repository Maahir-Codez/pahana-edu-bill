package com.pahanaedu.services;

import com.pahanaedu.dao.CustomerDAO;
import com.pahanaedu.dao.ICustomerDAO;
import com.pahanaedu.exceptions.ValidationException;
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

    @Override
    public Customer addCustomer(Customer customer) throws ValidationException {
        validateCustomerData(customer);

        return customerDAO.create(customer);
    }

    private void validateCustomerData(Customer customer) throws ValidationException {
        if (customer.getAccountNumber() == null || customer.getAccountNumber().trim().isEmpty()) {
            throw new ValidationException("Account Number cannot be empty.");
        }
        if (customerDAO.existsByAccountNumber(customer.getAccountNumber())) {
            throw new ValidationException("An account with this Account Number already exists.");
        }

        if (customer.getFullName() == null || customer.getFullName().trim().isEmpty()) {
            throw new ValidationException("Full Name cannot be empty.");
        }

        if (customer.getEmail() == null || customer.getEmail().trim().isEmpty()) {
            throw new ValidationException("Email cannot be empty.");
        }
        if (!customer.getEmail().matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            throw new ValidationException("Invalid email format.");
        }
        if (customerDAO.existsByEmail(customer.getEmail())) {
            throw new ValidationException("An account with this Email already exists.");
        }

    }
}