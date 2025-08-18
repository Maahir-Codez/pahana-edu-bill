package com.pahanaedu.services;

import com.pahanaedu.dao.IItemDAO;
import com.pahanaedu.dao.IOrderDAO;
import com.pahanaedu.dao.ItemDAO;
import com.pahanaedu.dao.OrderDAO;
import com.pahanaedu.exceptions.ValidationException;
import com.pahanaedu.models.Customer;
import com.pahanaedu.models.Item;
import com.pahanaedu.models.Order;
import com.pahanaedu.models.OrderItem;

import java.math.BigDecimal;
import java.util.Map;

public class OrderService implements IOrderService {

    private final IOrderDAO orderDAO;
    private final IItemDAO itemDAO;

    public OrderService() {
        this.orderDAO = new OrderDAO();
        this.itemDAO = new ItemDAO();
    }

    @Override
    public Order createOrder(Customer customer, Map<Item, Integer> itemsWithQuantities) throws ValidationException {
        if (customer == null || customer.getId() == null) {
            throw new ValidationException("Customer must be specified to create an order.");
        }
        if (itemsWithQuantities == null || itemsWithQuantities.isEmpty()) {
            throw new ValidationException("Order must contain at least one item.");
        }

        Order newOrder = new Order(customer.getId());
        BigDecimal subtotal = BigDecimal.ZERO;

        for (Map.Entry<Item, Integer> entry : itemsWithQuantities.entrySet()) {
            Item item = entry.getKey();
            Integer quantity = entry.getValue();

            if (quantity <= 0) {
                throw new ValidationException("Item quantity must be a positive number.");
            }

            if (item.getStockQuantity() < quantity) {
                throw new ValidationException("Not enough stock for item: " + item.getName() +
                        ". Requested: " + quantity + ", Available: " + item.getStockQuantity());
            }

            OrderItem orderItem = new OrderItem(item.getId(), null, quantity, item.getPrice());
            newOrder.getOrderItems().add(orderItem);

            subtotal = subtotal.add(orderItem.getLineTotal());
        }

        newOrder.setSubtotal(subtotal);
        newOrder.setTotalAmount(subtotal);

        Order savedOrder = orderDAO.createOrder(newOrder);

        for (Map.Entry<Item, Integer> entry : itemsWithQuantities.entrySet()) {
            Item item = entry.getKey();
            Integer quantity = entry.getValue();

            item.setStockQuantity(item.getStockQuantity() - quantity);
            itemDAO.update(item);
        }

        return savedOrder;
    }
}