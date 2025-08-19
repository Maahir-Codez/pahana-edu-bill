package com.pahanaedu.controllers;

import com.pahanaedu.api.dto.CustomerDTO;
import com.pahanaedu.clients.CustomerApiClient;
import com.pahanaedu.exceptions.ApiClientException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@WebServlet("/app/customers/*")
public class CustomerController extends HttpServlet {

    private CustomerApiClient customerApiClient;

    @Override
    public void init() {
        this.customerApiClient = new CustomerApiClient();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getPathInfo() == null ? "/list" : request.getPathInfo();

        try {
            switch (action) {
                case "/list":
                    listCustomers(request, response);
                    break;
                case "/add":
                    showAddForm(request, response);
                    break;
                case "/edit":
                    showEditForm(request, response);
                    break;
                default:
                    response.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (ApiClientException | InterruptedException e) {
            handleApiError(request, response, e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getPathInfo();
        if (action == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        try {
            switch (action) {
                case "/add":
                    addCustomer(request, response);
                    break;
                case "/edit":
                    updateCustomer(request, response);
                    break;
                case "/delete":
                    deleteCustomer(request, response);
                    break;
                default:
                    response.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (ApiClientException | InterruptedException e) {
            handleApiError(request, response, e);
        }
    }

    // --- GET Handlers ---
    private void listCustomers(HttpServletRequest request, HttpServletResponse response) throws IOException, InterruptedException, ApiClientException, ServletException {
        List<CustomerDTO> customers = customerApiClient.getAllCustomers();
        request.setAttribute("customerList", customers);
        request.getRequestDispatcher("/views/view-customers.jsp").forward(request, response);
    }

    private void showAddForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/views/add-customer.jsp").forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response) throws IOException, InterruptedException, ApiClientException, ServletException {
        long id = Long.parseLong(request.getParameter("id"));
        Optional<CustomerDTO> customerOpt = customerApiClient.getCustomerById(id);
        if (customerOpt.isPresent()) {
            request.setAttribute("customer", customerOpt.get());
            request.getRequestDispatcher("/views/edit-customer.jsp").forward(request, response);
        } else {
            request.setAttribute("errorMessage", "Customer not found.");
            listCustomers(request, response);
        }
    }

    // --- POST Handlers ---
    private void addCustomer(HttpServletRequest request, HttpServletResponse response) throws IOException, InterruptedException, ApiClientException, ServletException {
        CustomerDTO dto = extractCustomerDTOFromRequest(request);
        try {
            customerApiClient.createCustomer(dto);
            response.sendRedirect(request.getContextPath() + "/app/customers/list");
        } catch (ApiClientException e) {
            request.setAttribute("errorMessage", e.getMessage());
            request.setAttribute("customer", dto);
            showAddForm(request, response);
        }
    }

    private void updateCustomer(HttpServletRequest request, HttpServletResponse response) throws IOException, InterruptedException, ApiClientException, ServletException {
        long id = Long.parseLong(request.getParameter("id"));
        CustomerDTO dto = extractCustomerDTOFromRequest(request);
        dto.setId(id);
        try {
            customerApiClient.updateCustomer(dto);
            response.sendRedirect(request.getContextPath() + "/app/customers/list");
        } catch (ApiClientException e) {
            request.setAttribute("errorMessage", e.getMessage());
            request.setAttribute("customer", dto);
            request.getRequestDispatcher("/views/edit-customer.jsp").forward(request, response);
        }
    }

    private void deleteCustomer(HttpServletRequest request, HttpServletResponse response) throws IOException, InterruptedException, ApiClientException {
        long id = Long.parseLong(request.getParameter("id"));
        customerApiClient.deleteCustomer(id);
        response.sendRedirect(request.getContextPath() + "/app/customers/list");
    }

    // --- Helper Methods ---
    private CustomerDTO extractCustomerDTOFromRequest(HttpServletRequest request) {
        CustomerDTO dto = new CustomerDTO();
        dto.setAccountNumber(request.getParameter("accountNumber"));
        dto.setFullName(request.getParameter("fullName"));
        dto.setEmail(request.getParameter("email"));
        dto.setPhoneNumber(request.getParameter("phoneNumber"));
        dto.setAddress(request.getParameter("address"));
        dto.setCity(request.getParameter("city"));
        dto.setPostalCode(request.getParameter("postalCode"));
        return dto;
    }

    private void handleApiError(HttpServletRequest request, HttpServletResponse response, Exception e) throws ServletException, IOException {
        e.printStackTrace();
        request.setAttribute("errorMessage", "Error communicating with the backend service: " + e.getMessage());
        request.getRequestDispatcher("/views/dashboard.jsp").forward(request, response);
    }
}