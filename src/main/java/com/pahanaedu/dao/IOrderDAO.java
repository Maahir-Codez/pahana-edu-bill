package com.pahanaedu.dao;

import com.pahanaedu.models.Order;
import java.util.List;
import java.util.Optional;

public interface IOrderDAO {
    Order createOrder(Order order);
    Optional<Order> findById(long id);
    List<Order> findAll();
}