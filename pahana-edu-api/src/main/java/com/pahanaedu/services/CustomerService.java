package com.pahanaedu.services;

import com.pahanaedu.dao.CustomerDAO;
import com.pahanaedu.dao.ICustomerDAO;
import com.pahanaedu.exceptions.ValidationException;
import com.pahanaedu.models.Customer;

import java.time.LocalDateTime;
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
        validateCustomerData(customer, 0L);
        customer.setDateRegistered(LocalDateTime.now());
        customer.setActive(true);
        return customerDAO.create(customer);
    }

    @Override
    public void updateCustomer(Customer customer) throws ValidationException {
        Long customerId = customer.getId();
        if (customerId == null) {
            throw new ValidationException("Customer ID is required for an update.");
        }

        Optional<Customer> existingCustomerOpt = customerDAO.findById(customerId);
        if (!existingCustomerOpt.isPresent()) {
            throw new ValidationException("Cannot update. Customer with ID " + customerId + " not found.");
        }

        Customer existingCustomer = existingCustomerOpt.get();

        existingCustomer.setAccountNumber(customer.getAccountNumber());
        existingCustomer.setFullName(customer.getFullName());
        existingCustomer.setEmail(customer.getEmail());
        existingCustomer.setPhoneNumber(customer.getPhoneNumber());
        existingCustomer.setAddress(customer.getAddress());
        existingCustomer.setCity(customer.getCity());
        existingCustomer.setPostalCode(customer.getPostalCode());

        validateCustomerData(existingCustomer, existingCustomer.getId());

        customerDAO.update(existingCustomer);
    }

    private void validateCustomerData(Customer customer, Long customerIdToIgnore) throws ValidationException {
        if (customerIdToIgnore > 0 && customer.getId() == null) {
            throw new ValidationException("Customer ID is required for an update.");
        }

        if (customer.getAccountNumber() == null || customer.getAccountNumber().trim().isEmpty()) {
            throw new ValidationException("Account Number cannot be empty.");
        }
        if (customerDAO.existsByAccountNumber(customer.getAccountNumber(), customerIdToIgnore)) {
            throw new ValidationException("Another account with this Account Number already exists.");
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
        if (customerDAO.existsByEmail(customer.getEmail(), customerIdToIgnore)) {
            throw new ValidationException("Another account with this Email already exists.");
        }
    }

    @Override
    public void deactivateCustomer(long id) {
        customerDAO.deactivate(id);
    }
}