package com.pahanaedu.api.servlets;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pahanaedu.api.dto.CustomerDTO;
import com.pahanaedu.api.mappers.CustomerMapper;
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

@WebServlet("/api/customers/*")
public class CustomerApiServlet extends HttpServlet {
    private ICustomerService customerService;
    private Gson gson;

    @Override
    public void init() {
        this.customerService = new CustomerService();
        this.gson = new GsonBuilder().setPrettyPrinting().create();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String pathInfo = req.getPathInfo();
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        try {
            if (pathInfo == null || pathInfo.equals("/")) {
                List<Customer> customers = customerService.getAllCustomers();
                List<CustomerDTO> dtos = CustomerMapper.toDTOList(customers);
                resp.getWriter().write(gson.toJson(dtos));
            } else {
                long id = Long.parseLong(pathInfo.substring(1));
                Optional<Customer> customerOpt = customerService.getCustomerById(id);
                if (customerOpt.isPresent()) {
                    CustomerDTO dto = CustomerMapper.toDTO(customerOpt.get());
                    resp.getWriter().write(gson.toJson(dto));
                } else {
                    resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    resp.getWriter().write(gson.toJson(new ErrorResponse("Customer not found.")));
                }
            }
        } catch (NumberFormatException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write(gson.toJson(new ErrorResponse("Invalid ID format.")));
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        try {
            CustomerDTO dto = gson.fromJson(req.getReader(), CustomerDTO.class);
            Customer newCustomer = CustomerMapper.toModel(dto);
            Customer createdCustomer = customerService.addCustomer(newCustomer);
            CustomerDTO createdDto = CustomerMapper.toDTO(createdCustomer);

            resp.setStatus(HttpServletResponse.SC_CREATED);
            resp.getWriter().write(gson.toJson(createdDto));
        } catch (ValidationException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write(gson.toJson(new ErrorResponse(e.getMessage())));
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write(gson.toJson(new ErrorResponse("An unexpected error occurred.")));
            e.printStackTrace();
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String pathInfo = req.getPathInfo();
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        if (pathInfo == null || pathInfo.equals("/")) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write(gson.toJson(new ErrorResponse("Customer ID is required.")));
            return;
        }

        try {
            long id = Long.parseLong(pathInfo.substring(1));
            CustomerDTO dto = gson.fromJson(req.getReader(), CustomerDTO.class);
            Customer customerToUpdate = CustomerMapper.toModel(dto);
            customerToUpdate.setId(id);

            customerService.updateCustomer(customerToUpdate);
            CustomerDTO updatedDto = CustomerMapper.toDTO(customerToUpdate);
            resp.getWriter().write(gson.toJson(updatedDto));
        } catch (ValidationException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write(gson.toJson(new ErrorResponse(e.getMessage())));
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write(gson.toJson(new ErrorResponse("An unexpected error occurred.")));
            e.printStackTrace();
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String pathInfo = req.getPathInfo();
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        if (pathInfo == null || pathInfo.equals("/")) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write(gson.toJson(new ErrorResponse("Customer ID is required.")));
            return;
        }

        try {
            long id = Long.parseLong(pathInfo.substring(1));
            customerService.deactivateCustomer(id);
            resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
        } catch (NumberFormatException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write(gson.toJson(new ErrorResponse("Invalid ID format.")));
        }
    }

    private static class ErrorResponse {
        private final String error;
        public ErrorResponse(String error) { this.error = error; }
    }
}