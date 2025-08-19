package com.pahanaedu.controllers;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pahanaedu.api.dto.CustomerDTO;
import com.pahanaedu.api.dto.ItemDTO;
import com.pahanaedu.api.dto.OrderDTO;
import com.pahanaedu.api.dto.OrderRequestDTO;
import com.pahanaedu.clients.CustomerApiClient;
import com.pahanaedu.clients.ItemApiClient;
import com.pahanaedu.clients.OrderApiClient;
import com.pahanaedu.exceptions.ApiClientException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/app/orders/*")
public class OrderController extends HttpServlet {

    private OrderApiClient orderApiClient;
    private CustomerApiClient customerApiClient;
    private ItemApiClient itemApiClient;
    private Gson gson;

    @Override
    public void init() {
        this.orderApiClient = new OrderApiClient();
        this.customerApiClient = new CustomerApiClient();
        this.itemApiClient = new ItemApiClient();
        this.gson = new Gson();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getPathInfo();
        if (action == null) action = "/list";

        try {
            switch (action) {
                case "/create":
                    showCreateOrderForm(request, response);
                    break;
                case "/list":
                    listOrders(request, response);
                    break;
                case "/view":
                    viewOrder(request, response);
                    break;
                default:
                    response.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (ApiClientException | InterruptedException e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Error communicating with the service: " + e.getMessage());
            request.getRequestDispatcher("/views/dashboard.jsp").forward(request, response);
        }
    }

    private void showCreateOrderForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, ApiClientException, InterruptedException {
        List<CustomerDTO> customers = customerApiClient.getAllCustomers();
        List<ItemDTO> items = itemApiClient.getAllItems();

        request.setAttribute("customers", customers);
        request.setAttribute("items", items);
        request.getRequestDispatcher("/views/create-order.jsp").forward(request, response);
    }

    private void listOrders(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, ApiClientException, InterruptedException {
        List<OrderDTO> orders = orderApiClient.getAllOrders();
        request.setAttribute("orderList", orders);
        request.getRequestDispatcher("/views/view-orders.jsp").forward(request, response);
    }

    private void viewOrder(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, ApiClientException, InterruptedException {
        long orderId = Long.parseLong(request.getParameter("id"));
        OrderDTO order = orderApiClient.getOrderById(orderId);

        if (order == null) {
            request.setAttribute("errorMessage", "Order with ID " + orderId + " not found.");
            listOrders(request, response);
            return;
        }

        CustomerDTO customer = customerApiClient.getCustomerById(order.getCustomerId()).orElse(null);

        request.setAttribute("order", order);
        request.setAttribute("customer", customer);
        request.getRequestDispatcher("/views/print-bill.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getPathInfo();
        if ("/create".equals(action)) {
            createOrder(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private void createOrder(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            long customerId = Long.parseLong(request.getParameter("customerId"));

            String[] itemIds = request.getParameterValues("itemIds");
            String[] quantities = request.getParameterValues("quantities");

            if (itemIds == null || quantities == null || itemIds.length == 0) {
                throw new ApiClientException("Cannot create an order with an empty cart.", 400);
            }

            Map<Long, Integer> cart = new HashMap<>();
            for (int i = 0; i < itemIds.length && i < quantities.length; i++) {
                long itemId = Long.parseLong(itemIds[i]);
                int quantity = Integer.parseInt(quantities[i]);
                cart.put(itemId, quantity);
            }

            if (cart.isEmpty()) {
                throw new ApiClientException("Cannot create an order with an empty cart.", 400);
            }

            OrderRequestDTO orderRequest = new OrderRequestDTO();
            orderRequest.setCustomerId(customerId);
            orderRequest.setCart(cart);

            Map<String, Object> result = orderApiClient.createOrder(orderRequest);

            long orderId = ((Double)result.get("orderId")).longValue();
            response.sendRedirect(request.getContextPath() + "/app/dashboard?status=order_success&orderId=" + orderId);

        } catch (ApiClientException e) {
            request.setAttribute("errorMessage", e.getMessage());
            try {
                showCreateOrderForm(request, response);
            } catch (Exception loadEx) {
                loadEx.printStackTrace();
                response.sendRedirect(request.getContextPath() + "/app/dashboard?status=order_error");
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "An unexpected error occurred: " + e.getMessage());
            try {
                showCreateOrderForm(request, response);
            } catch (Exception loadEx) {
                response.sendRedirect(request.getContextPath() + "/app/dashboard?status=order_error");
            }
        }
    }
}