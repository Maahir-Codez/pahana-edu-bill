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
        String insertOrderSQL = "INSERT INTO orders (customer_id, order_date, status, subtotal, total_amount) VALUES (?, ?, ?, ?, ?)";
        String insertOrderItemSQL = "INSERT INTO order_items (order_id, item_id, quantity, price_at_purchase, line_total) VALUES (?, ?, ?, ?, ?)";
        String updateStockSQL = "UPDATE items SET stock_quantity = stock_quantity - ? WHERE id = ?";

        try {
            connection.setAutoCommit(false);

            try (PreparedStatement orderStmt = connection.prepareStatement(insertOrderSQL, Statement.RETURN_GENERATED_KEYS)) {

                orderStmt.setLong(1, order.getCustomerId());
                orderStmt.setTimestamp(2, Timestamp.valueOf(order.getOrderDate()));
                orderStmt.setString(3, order.getStatus().name());
                orderStmt.setBigDecimal(4, order.getSubtotal());
                orderStmt.setBigDecimal(5, order.getTotalAmount());
                orderStmt.executeUpdate();

                try (ResultSet generatedKeys = orderStmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        order.setId(generatedKeys.getLong(1));
                    } else {
                        throw new SQLException("Creating order failed, no ID obtained.");
                    }
                }
            }

            try (PreparedStatement itemStmt = connection.prepareStatement(insertOrderItemSQL)) {
                for (OrderItem item : order.getOrderItems()) {
                    itemStmt.setLong(1, order.getId());
                    itemStmt.setLong(2, item.getItemId());
                    itemStmt.setInt(3, item.getQuantity());
                    itemStmt.setBigDecimal(4, item.getPriceAtPurchase());
                    itemStmt.setBigDecimal(5, item.getLineTotal());
                    itemStmt.addBatch();
                }
                itemStmt.executeBatch();
            }

            try (PreparedStatement stockStmt = connection.prepareStatement(updateStockSQL)) {
                for (OrderItem item : order.getOrderItems()) {
                    stockStmt.setInt(1, item.getQuantity());
                    stockStmt.setLong(2, item.getItemId());
                    stockStmt.addBatch();
                }
                stockStmt.executeBatch();
            }

            connection.commit();
            return order;

        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
            e.printStackTrace();
            throw new RuntimeException("Error creating order. Transaction was rolled back.", e);
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException finalEx) {
                finalEx.printStackTrace();
            }
        }
    }

    @Override
    public Optional<Order> findById(long id) {
        String findOrderSQL = "SELECT * FROM orders WHERE id = ?";
        String findItemsSQL = "SELECT * FROM order_items WHERE order_id = ?";

        Optional<Order> orderOptional = Optional.empty();

        try (PreparedStatement orderStmt = connection.prepareStatement(findOrderSQL)) {
            orderStmt.setLong(1, id);
            try (ResultSet rs = orderStmt.executeQuery()) {
                if (rs.next()) {
                    Order order = mapResultSetToOrder(rs);

                    try (PreparedStatement itemStmt = connection.prepareStatement(findItemsSQL)) {
                        itemStmt.setLong(1, id);
                        try (ResultSet itemRs = itemStmt.executeQuery()) {
                            List<OrderItem> orderItems = new ArrayList<>();
                            while (itemRs.next()) {
                                orderItems.add(mapResultSetToOrderItem(itemRs));
                            }
                            order.setOrderItems(orderItems);
                        }
                    }
                    orderOptional = Optional.of(order);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orderOptional;
    }

    @Override
    public List<Order> findAll() {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT * FROM orders ORDER BY order_date DESC";

        try (Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(sql)) {
            while (rs.next()) {
                orders.add(mapResultSetToOrder(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }


    private Order mapResultSetToOrder(ResultSet rs) throws SQLException {
        Order order = new Order();
        order.setId(rs.getLong("id"));
        order.setCustomerId(rs.getLong("customer_id"));
        order.setOrderDate(rs.getTimestamp("order_date").toLocalDateTime());
        order.setStatus(OrderStatus.valueOf(rs.getString("status")));
        order.setSubtotal(rs.getBigDecimal("subtotal"));
        order.setTotalAmount(rs.getBigDecimal("total_amount"));
        return order;
    }

    private OrderItem mapResultSetToOrderItem(ResultSet rs) throws SQLException {
        OrderItem item = new OrderItem();
        item.setId(rs.getLong("id"));
        item.setOrderId(rs.getLong("order_id"));
        item.setItemId(rs.getLong("item_id"));
        item.setQuantity(rs.getInt("quantity"));
        item.setPriceAtPurchase(rs.getBigDecimal("price_at_purchase"));
        item.setLineTotal(rs.getBigDecimal("line_total"));
        return item;
    }
}