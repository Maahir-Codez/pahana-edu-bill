package com.pahanaedu.services;

import com.pahanaedu.dao.IOrderDAO;
import com.pahanaedu.dao.OrderDAO;
import com.pahanaedu.exceptions.ValidationException;
import com.pahanaedu.models.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class OrderService implements IOrderService {

    private final IOrderDAO orderDAO;

    public OrderService() {
        this.orderDAO = new OrderDAO();
    }

    @Override
    public Order placeOrder(Customer customer, Map<Item, Integer> itemsWithQuantities) throws ValidationException {
        if (customer == null || customer.getId() == null) {
            throw new ValidationException("A valid customer is required to place an order.");
        }
        if (itemsWithQuantities == null || itemsWithQuantities.isEmpty()) {
            throw new ValidationException("Order must contain at least one item.");
        }

        Order order = new Order(customer.getId());
        BigDecimal subtotal = BigDecimal.ZERO;

        for (Map.Entry<Item, Integer> entry : itemsWithQuantities.entrySet()) {
            Item item = entry.getKey();
            int quantity = entry.getValue();

            if (quantity <= 0) {
                throw new ValidationException("Item quantity must be positive for " + item.getName());
            }
            if (item.getStockQuantity() < quantity) {
                throw new ValidationException("Not enough stock for item: " + item.getName() +
                        ". Requested: " + quantity + ", Available: " + item.getStockQuantity());
            }

            OrderItem orderItem = new OrderItem(item.getId(), null, quantity, item.getPrice());
            order.getOrderItems().add(orderItem);
        }

        subtotal = order.getOrderItems().stream()
                .map(OrderItem::getLineTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        order.setSubtotal(subtotal);


        order.setTotalAmount(subtotal);
        order.setStatus(OrderStatus.PAID);

        return orderDAO.createOrder(order);
    }

    @Override
    public Optional<Order> getOrderById(long id) {
        return orderDAO.findById(id);
    }

    @Override
    public List<Order> getAllOrders() {
        return orderDAO.findAll();
    }
}