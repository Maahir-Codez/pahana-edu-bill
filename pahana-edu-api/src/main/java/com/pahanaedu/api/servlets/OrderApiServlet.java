package com.pahanaedu.api.servlets;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pahanaedu.api.dto.OrderDTO;
import com.pahanaedu.api.dto.OrderRequestDTO;
import com.pahanaedu.api.mappers.OrderMapper;
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
import java.util.stream.Collectors;
import java.util.function.Function;



@WebServlet("/api/orders/*")
public class OrderApiServlet extends HttpServlet {

    private IOrderService orderService;
    private ICustomerService customerService;
    private IItemService itemService;
    private Gson gson;

    @Override
    public void init() {
        this.orderService = new OrderService();
        this.customerService = new CustomerService();
        this.itemService = new ItemService();
        this.gson = new GsonBuilder().setPrettyPrinting().create();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            createOrder(req, resp);
        } else {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String pathInfo = req.getPathInfo();
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        try {
            if (pathInfo == null || pathInfo.equals("/")) {
                List<Order> orders = orderService.getAllOrders();

                List<Customer> customers = customerService.getAllCustomers();
                Map<Long, Customer> customersById = customers.stream()
                        .collect(Collectors.toMap(Customer::getId, Function.identity()));

                List<OrderDTO> orderDTOs = OrderMapper.toDTOList(orders, customersById);

                resp.getWriter().write(gson.toJson(orderDTOs));

            } else {
                long id = Long.parseLong(pathInfo.substring(1));
                Optional<Order> orderOpt = orderService.getOrderById(id);

                if (orderOpt.isPresent()) {
                    Order order = orderOpt.get();

                    Optional<Customer> customerOpt = customerService.getCustomerById(order.getCustomerId());
                    if (!customerOpt.isPresent()) {
                        throw new Exception("Could not find customer for order ID " + id);
                    }
                    Customer customer = customerOpt.get();

                    OrderDTO dto = OrderMapper.toDTO(order, customer);

                    resp.getWriter().write(gson.toJson(dto));
                } else {
                    resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    resp.getWriter().write(gson.toJson(Map.of("error", "Order not found.")));
                }
            }
        } catch (NumberFormatException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write(gson.toJson(Map.of("error", "Invalid Order ID format.")));
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write(gson.toJson(Map.of("error", "An unexpected error occurred.")));
            e.printStackTrace();
        }
    }

    private void createOrder(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        try {
            OrderRequestDTO requestDTO = gson.fromJson(req.getReader(), OrderRequestDTO.class);

            Optional<Customer> customerOpt = customerService.getCustomerById(requestDTO.getCustomerId());
            if (!customerOpt.isPresent()) {
                throw new ValidationException("Customer with ID " + requestDTO.getCustomerId() + " not found.");
            }
            Customer customer = customerOpt.get();

            Map<Item, Integer> itemsWithQuantities = new HashMap<>();
            for (Map.Entry<Long, Integer> entry : requestDTO.getCart().entrySet()) {
                Long itemId = entry.getKey();
                Integer quantity = entry.getValue();

                Optional<Item> itemOpt = itemService.getItemById(itemId);
                if (!itemOpt.isPresent()) {
                    throw new ValidationException("Item with ID " + itemId + " not found in cart.");
                }
                itemsWithQuantities.put(itemOpt.get(), quantity);
            }

            Order createdOrder = orderService.placeOrder(customer, itemsWithQuantities);

            resp.setStatus(HttpServletResponse.SC_CREATED);
            resp.getWriter().write(gson.toJson(Map.of("message", "Order created successfully", "orderId", createdOrder.getId())));

        } catch (ValidationException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write(gson.toJson(Map.of("error", e.getMessage())));
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write(gson.toJson(Map.of("error", "An unexpected server error occurred.")));
            e.printStackTrace();
        }
    }
}