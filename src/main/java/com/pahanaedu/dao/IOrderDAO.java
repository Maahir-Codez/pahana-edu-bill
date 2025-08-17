package com.pahanaedu.dao;

import com.pahanaedu.models.Order;
import java.util.List;
import java.util.Optional;

public interface IOrderDAO {
    Order createOrder(Order order);
    Optional<Order> findOrderById(long id);
    List<Order> findOrdersByCustomerId(long customerId);
}