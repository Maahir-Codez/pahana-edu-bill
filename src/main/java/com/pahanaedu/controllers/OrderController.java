package com.pahanaedu.controllers;

import com.pahanaedu.exceptions.ValidationException;
import com.pahanaedu.models.Customer;
import com.pahanaedu.models.Item;
import com.pahanaedu.models.Order;
import com.pahanaedu.services.*;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@WebServlet("/orders")
public class OrderController extends HttpServlet {

    private IOrderService orderService;
    private ICustomerService customerService;
    private IItemService itemService;

    @Override
    public void init() {
        this.orderService = new OrderService();
        this.customerService = new CustomerService();
        this.itemService = new ItemService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) {
            action = "create";
        };

        if ("create".equals(action)) {
            showCreateOrderForm(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if ("create".equals(action)) {
            createOrder(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private void showCreateOrderForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Customer> customers = customerService.getAllCustomers();
        List<Item> items = itemService.getAllItems();

        request.setAttribute("customers", customers);
        request.setAttribute("items", items);
        request.getRequestDispatcher("/views/create-order.jsp").forward(request, response);
    }

    private void createOrder(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            long customerId = Long.parseLong(request.getParameter("customerId"));
            Optional<Customer> customerOpt = customerService.getCustomerById(customerId);
            if (!customerOpt.isPresent()) {
                throw new ValidationException("Selected customer not found.");
            }
            Customer customer = customerOpt.get();

            Map<Item, Integer> itemsWithQuantities = new HashMap<>();
            String[] itemIds = request.getParameterValues("itemIds[]");
            String[] quantities = request.getParameterValues("quantities[]");

            if (itemIds == null || quantities == null || itemIds.length != quantities.length) {
                throw new ValidationException("Order items data is corrupted or empty.");
            }

            for (int i = 0; i < itemIds.length; i++) {
                long itemId = Long.parseLong(itemIds[i]);
                int quantity = Integer.parseInt(quantities[i]);

                Optional<Item> itemOpt = itemService.getItemById(itemId);
                if (!itemOpt.isPresent()) {
                    throw new ValidationException("Item with ID " + itemId + " not found.");
                }
                itemsWithQuantities.put(itemOpt.get(), quantity);
            }

            Order newOrder = orderService.createOrder(customer, itemsWithQuantities);

            response.sendRedirect(request.getContextPath() + "/dashboard?status=order_success");

        } catch (ValidationException | NumberFormatException e) {
            request.setAttribute("errorMessage", e.getMessage());
            showCreateOrderForm(request, response);
        }
    }
}