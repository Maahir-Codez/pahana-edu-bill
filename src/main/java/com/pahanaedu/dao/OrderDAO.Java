package com.pahanaedu.dao;

import com.pahanaedu.models.Order;
import com.pahanaedu.models.OrderItem;
import com.pahanaedu.models.OrderStatus;
import com.pahanaedu.utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class OrderDAO implements IOrderDAO {

    private final Connection connection;

    public OrderDAO() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    @Override
    public Order createOrder(Order order) {
        String insertOrderSQL = "INSERT INTO orders (customer_id, order_date, status, subtotal, discount_amount, tax_amount, total_amount) VALUES (?, ?, ?, ?, ?, ?, ?)";
        String insertOrderItemSQL = "INSERT INTO order_items (order_id, item_id, quantity, price_at_purchase, line_total) VALUES (?, ?, ?, ?, ?)";

        try {
            connection.setAutoCommit(false);

            try (PreparedStatement orderStmt = connection.prepareStatement(insertOrderSQL, Statement.RETURN_GENERATED_KEYS)) {
                orderStmt.setLong(1, order.getCustomerId());
                orderStmt.setTimestamp(2, Timestamp.valueOf(order.getOrderDate()));
                orderStmt.setString(3, order.getStatus().name());
                orderStmt.setBigDecimal(4, order.getSubtotal());
                orderStmt.setBigDecimal(5, order.getDiscountAmount());
                orderStmt.setBigDecimal(6, order.getTaxAmount());
                orderStmt.setBigDecimal(7, order.getTotalAmount());
                orderStmt.executeUpdate();

                try (ResultSet generatedKeys = orderStmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        order.setId(generatedKeys.getLong(1));
                    } else {
                        throw new SQLException("Creating order failed, no ID obtained.");
                    }
                }
            }

            try (PreparedStatement orderItemStmt = connection.prepareStatement(insertOrderItemSQL)) {
                for (OrderItem item : order.getOrderItems()) {
                    orderItemStmt.setLong(1, order.getId()); // Use the new order ID
                    orderItemStmt.setLong(2, item.getItemId());
                    orderItemStmt.setInt(3, item.getQuantity());
                    orderItemStmt.setBigDecimal(4, item.getPriceAtPurchase());
                    orderItemStmt.setBigDecimal(5, item.getLineTotal());
                    orderItemStmt.addBatch(); // Add this statement to a batch for efficient execution
                }
                orderItemStmt.executeBatch();
            }

            connection.commit();

        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
            e.printStackTrace();
            throw new RuntimeException("Transaction failed. Error creating order.", e);
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException finalEx) {
                finalEx.printStackTrace();
            }
        }

        return order;
    }

    @Override
    public Optional<Order> findOrderById(long id) {
        return Optional.empty();
    }

    @Override
    public List<Order> findOrdersByCustomerId(long customerId) {
        return new ArrayList<>();
    }
}