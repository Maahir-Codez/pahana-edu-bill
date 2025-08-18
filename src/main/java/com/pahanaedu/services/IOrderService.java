package com.pahanaedu.services;

import com.pahanaedu.exceptions.ValidationException;
import com.pahanaedu.models.Customer;
import com.pahanaedu.models.Item;
import com.pahanaedu.models.Order;

import java.util.Map;

public interface IOrderService {
    Order createOrder(Customer customer, Map<Item, Integer> itemsWithQuantities) throws ValidationException;
}