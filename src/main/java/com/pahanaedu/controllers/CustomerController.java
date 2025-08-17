package com.pahanaedu.controllers;

import com.pahanaedu.exceptions.ValidationException;
import com.pahanaedu.models.Customer;
import com.pahanaedu.services.CustomerService;
import com.pahanaedu.services.ICustomerService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@WebServlet("/customers/*")
public class CustomerController extends HttpServlet {
    private ICustomerService customerService;

    @Override
    public void init() throws ServletException {
        this.customerService = new CustomerService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getPathInfo();
        if (action == null) {
            action = "/list";
        }

        switch (action) {
            case "/list":
                listCustomers(request, response);
                break;
            case "/add":
                showAddCustomerForm(request, response);
                break;
            case "/edit":
                showEditCustomerForm(request, response);
                break;
            default:
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getPathInfo();
        if (action == null) { response.sendError(HttpServletResponse.SC_BAD_REQUEST); return; }

        switch(action) {
            case "/add":
                addCustomer(request, response);
                break;
            case "/edit":
                updateCustomer(request, response);
                break;
            default:
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private void listCustomers(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Customer> customers = customerService.getAllCustomers();

        request.setAttribute("customerList", customers);

        request.getRequestDispatcher("/views/view-customers.jsp").forward(request, response);
    }

    private void showAddCustomerForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/views/add-customer.jsp").forward(request, response);
    }

    private void addCustomer(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String accountNumber = request.getParameter("accountNumber");
        String fullName = request.getParameter("fullName");
        String email = request.getParameter("email");
        String phoneNumber = request.getParameter("phoneNumber");
        String address = request.getParameter("address");
        String city = request.getParameter("city");
        String postalCode = request.getParameter("postalCode");

        Customer newCustomer = new Customer(accountNumber, fullName, address, city, postalCode, phoneNumber, email);

        try {
            customerService.addCustomer(newCustomer);

            response.sendRedirect(request.getContextPath() + "/customers/list");

        } catch (ValidationException e) {
           request.setAttribute("errorMessage", e.getMessage());

            request.setAttribute("customer", newCustomer);

            showAddCustomerForm(request, response);
        }
    }

    private void showEditCustomerForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            long id = Long.parseLong(request.getParameter("id"));
            Optional<Customer> customerOptional = customerService.getCustomerById(id);

            if (customerOptional.isPresent()) {
                request.setAttribute("customer", customerOptional.get());
                request.getRequestDispatcher("/views/edit-customer.jsp").forward(request, response);
            } else {
                request.setAttribute("errorMessage", "Customer not found.");
                listCustomers(request, response); // Redirect to the list view
            }
        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "Invalid customer ID.");
            listCustomers(request, response);
        }
    }

    private void updateCustomer(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            long id = Long.parseLong(request.getParameter("id"));
            String accountNumber = request.getParameter("accountNumber");
            String fullName = request.getParameter("fullName");
            String email = request.getParameter("email");
            String phoneNumber = request.getParameter("phoneNumber");
            String address = request.getParameter("address");
            String city = request.getParameter("city");
            String postalCode = request.getParameter("postalCode");

            Optional<Customer> existingCustomerOpt = customerService.getCustomerById(id);
            if (!existingCustomerOpt.isPresent()) {
                throw new ValidationException("Cannot update. Customer not found.");
            }

            Customer customerToUpdate = existingCustomerOpt.get();
            customerToUpdate.setAccountNumber(accountNumber);
            customerToUpdate.setFullName(fullName);
            customerToUpdate.setEmail(email);
            customerToUpdate.setPhoneNumber(phoneNumber);
            customerToUpdate.setAddress(address);
            customerToUpdate.setCity(city);
            customerToUpdate.setPostalCode(postalCode);

            customerService.updateCustomer(customerToUpdate);

            response.sendRedirect(request.getContextPath() + "/customers/list");

        } catch (ValidationException | NumberFormatException e) {
            request.setAttribute("errorMessage", e.getMessage());

            long id = Long.parseLong(request.getParameter("id"));
            Customer customerWithError = new Customer();
            customerWithError.setId(id);
            request.setAttribute("customer", customerWithError);

            request.getRequestDispatcher("/views/edit-customer.jsp").forward(request, response);
        }
    }

}