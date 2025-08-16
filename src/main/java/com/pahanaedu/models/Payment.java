package com.pahanaedu.models;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Payment {

    private Long id;
    private Long orderId;
    private BigDecimal amount;
    private String paymentMethod;
    private String transactionId;
    private PaymentStatus status;
    private LocalDateTime paymentDate;

    public Payment() {
        this.paymentDate = LocalDateTime.now();
        this.status = PaymentStatus.PENDING;
    }

    public Payment(Long orderId, BigDecimal amount, String paymentMethod) {
        this(); // Call default constructor
        this.orderId = orderId;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
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

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public void setStatus(PaymentStatus status) {
        this.status = status;
    }

    public LocalDateTime getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDateTime paymentDate) {
        this.paymentDate = paymentDate;
    }

    @Override
    public String toString() {
        return "Payment{" +
                "id=" + id +
                ", orderId=" + orderId +
                ", amount=" + amount +
                ", status=" + status +
                ", paymentDate=" + paymentDate +
                '}';
    }
}