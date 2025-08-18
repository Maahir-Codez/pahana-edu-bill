package com.pahanaedu.services;

import com.pahanaedu.exceptions.ValidationException;
import com.pahanaedu.models.Customer;
import com.pahanaedu.models.Item;
import com.pahanaedu.models.Order;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface IOrderService {
    Order placeOrder(Customer customer, Map<Item, Integer> itemsWithQuantities) throws ValidationException;
    Optional<Order> getOrderById(long id);
    List<Order> getAllOrders();
}