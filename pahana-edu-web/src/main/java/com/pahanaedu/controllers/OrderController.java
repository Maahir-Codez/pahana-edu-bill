//package com.pahanaedu.controllers;
//
//import com.pahanaedu.exceptions.ValidationException;
//import com.pahanaedu.models.Customer;
//import com.pahanaedu.models.Item;
//import com.pahanaedu.models.Order;
//import com.pahanaedu.models.OrderItem;
//import com.pahanaedu.services.*;
//
//import jakarta.servlet.ServletException;
//import jakarta.servlet.annotation.WebServlet;
//import jakarta.servlet.http.HttpServlet;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.Optional;
//
//@WebServlet("/orders")
//public class OrderController extends HttpServlet {
//
//    private IOrderService orderService;
//    private ICustomerService customerService;
//    private IItemService itemService;
//
//    @Override
//    public void init() {
//        this.orderService = new OrderService();
//        this.customerService = new CustomerService();
//        this.itemService = new ItemService();
//    }
//
//    @Override
//    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        String action = request.getParameter("action");
//        if (action == null) {
//            action = "list";
//        };
//
//        switch (action) {
//            case "create":
//                showCreateOrderForm(request, response);
//                break;
//            case "list":
//                listOrders(request, response);
//                break;
//            case "view":
//                viewOrder(request, response);
//                break;
//            default:
//                response.sendError(HttpServletResponse.SC_NOT_FOUND);
//        }
//    }
//
//    @Override
//    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        String action = request.getParameter("action");
//        if ("create".equals(action)) {
//            createOrder(request, response);
//        } else {
//            response.sendError(HttpServletResponse.SC_NOT_FOUND);
//        }
//    }
//
//    private void showCreateOrderForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        List<Customer> customers = customerService.getAllCustomers();
//        List<Item> items = itemService.getAllItems();
//
//        request.setAttribute("customers", customers);
//        request.setAttribute("items", items);
//        request.getRequestDispatcher("/views/create-order.jsp").forward(request, response);
//    }
//
//    private void createOrder(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        try {
//            long customerId = Long.parseLong(request.getParameter("customerId"));
//            Optional<Customer> customerOpt = customerService.getCustomerById(customerId);
//            if (!customerOpt.isPresent()) {
//                throw new ValidationException("Selected customer not found.");
//            }
//            Customer customer = customerOpt.get();
//
//            Map<Item, Integer> itemsWithQuantities = new HashMap<>();
//            String[] itemIds = request.getParameterValues("itemIds[]");
//            String[] quantities = request.getParameterValues("quantities[]");
//
//            if (itemIds == null || quantities == null || itemIds.length != quantities.length) {
//                throw new ValidationException("Order items data is corrupted or empty.");
//            }
//
//            for (int i = 0; i < itemIds.length; i++) {
//                long itemId = Long.parseLong(itemIds[i]);
//                int quantity = Integer.parseInt(quantities[i]);
//
//                Optional<Item> itemOpt = itemService.getItemById(itemId);
//                if (!itemOpt.isPresent()) {
//                    throw new ValidationException("Item with ID " + itemId + " not found.");
//                }
//                itemsWithQuantities.put(itemOpt.get(), quantity);
//            }
//
//            Order newOrder = orderService.placeOrder(customer, itemsWithQuantities);
//
//            response.sendRedirect(request.getContextPath() + "/dashboard?status=order_success");
//
//        } catch (ValidationException | NumberFormatException e) {
//            request.setAttribute("errorMessage", e.getMessage());
//            showCreateOrderForm(request, response);
//        }
//    }
//
//    private void listOrders(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        List<Order> orders = orderService.getAllOrders();
//        List<Customer> customers = customerService.getAllCustomers();
//
//        Map<Long, String> customerMap = new HashMap<>();
//        for (Customer c : customers) {
//            customerMap.put(c.getId(), c.getFullName());
//        }
//
//        request.setAttribute("orderList", orders);
//        request.setAttribute("customerMap", customerMap);
//        request.getRequestDispatcher("/views/view-orders.jsp").forward(request, response);
//    }
//
//    private void viewOrder(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        try {
//            long orderId = Long.parseLong(request.getParameter("id"));
//            Optional<Order> orderOptional = orderService.getOrderById(orderId);
//
//            if (orderOptional.isPresent()) {
//                Order order = orderOptional.get();
//                Optional<Customer> customerOptional = customerService.getCustomerById(order.getCustomerId());
//
//                for (OrderItem oi : order.getOrderItems()) {
//                    itemService.getItemById(oi.getItemId()).ifPresent(oi::setItem);
//                }
//
//                request.setAttribute("order", order);
//                customerOptional.ifPresent(c -> request.setAttribute("customer", c));
//                request.getRequestDispatcher("/views/print-bill.jsp").forward(request, response);
//            } else {
//                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Order not found.");
//            }
//        } catch (NumberFormatException e) {
//            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid Order ID.");
//        }
//    }
//}