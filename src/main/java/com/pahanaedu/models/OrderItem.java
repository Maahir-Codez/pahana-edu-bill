package com.pahanaedu.models;

import java.math.BigDecimal;

public class OrderItem {

    private Long id;
    private Long orderId;
    private Long itemId;
    private int quantity;
    private BigDecimal priceAtPurchase;
    private BigDecimal lineTotal;

    public OrderItem() {
    }

    public OrderItem(Long itemId, Long orderId, int quantity, BigDecimal priceAtPurchase) {
        this.itemId = itemId;
        this.orderId = orderId;
        this.quantity = quantity;
        this.priceAtPurchase = priceAtPurchase;
        this.lineTotal = priceAtPurchase.multiply(new BigDecimal(quantity));
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPriceAtPurchase() {
        return priceAtPurchase;
    }

    public void setPriceAtPurchase(BigDecimal priceAtPurchase) {
        this.priceAtPurchase = priceAtPurchase;
    }

    public BigDecimal getLineTotal() {
        return lineTotal;
    }

    public void setLineTotal(BigDecimal lineTotal) {
        this.lineTotal = lineTotal;
    }

    @Override
    public String toString() {
        return "OrderItem{" +
                "id=" + id +
                ", orderId=" + orderId +
                ", itemId=" + itemId +
                ", quantity=" + quantity +
                ", priceAtPurchase=" + priceAtPurchase +
                '}';
    }
}