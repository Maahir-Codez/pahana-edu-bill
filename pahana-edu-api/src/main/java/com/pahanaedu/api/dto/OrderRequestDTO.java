package com.pahanaedu.api.dto;

import java.util.Map;

public class OrderRequestDTO {
    private long customerId;
    private Map<Long, Integer> cart;

    public long getCustomerId() { return customerId; }
    public void setCustomerId(long customerId) { this.customerId = customerId; }
    public Map<Long, Integer> getCart() { return cart; }
    public void setCart(Map<Long, Integer> cart) { this.cart = cart; }
}