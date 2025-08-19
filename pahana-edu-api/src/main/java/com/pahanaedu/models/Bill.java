package com.pahanaedu.models;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Bill {

    private Long id;
    private Long customerId;
    private String billNumber;
    private LocalDate billingPeriodStart;
    private LocalDate billingPeriodEnd;
    private double unitsConsumed;
    private BigDecimal pricePerUnit;
    private BigDecimal amountDue;
    private LocalDate dueDate;
    private boolean isPaid;

    public Bill() {
        this.billingPeriodStart = LocalDate.now().withDayOfMonth(1);
        this.billingPeriodEnd = LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth());
        this.dueDate = LocalDate.now().plusMonths(1).withDayOfMonth(15);
        this.isPaid = false;
    }

    public Bill(Long customerId, double unitsConsumed, BigDecimal pricePerUnit) {
        this();
        this.customerId = customerId;
        this.unitsConsumed = unitsConsumed;
        this.pricePerUnit = pricePerUnit;
        this.amountDue = pricePerUnit.multiply(BigDecimal.valueOf(unitsConsumed));
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getBillNumber() {
        return billNumber;
    }

    public void setBillNumber(String billNumber) {
        this.billNumber = billNumber;
    }

    public LocalDate getBillingPeriodStart() {
        return billingPeriodStart;
    }

    public void setBillingPeriodStart(LocalDate billingPeriodStart) {
        this.billingPeriodStart = billingPeriodStart;
    }

    public LocalDate getBillingPeriodEnd() {
        return billingPeriodEnd;
    }

    public void setBillingPeriodEnd(LocalDate billingPeriodEnd) {
        this.billingPeriodEnd = billingPeriodEnd;
    }

    public double getUnitsConsumed() {
        return unitsConsumed;
    }

    public void setUnitsConsumed(double unitsConsumed) {
        this.unitsConsumed = unitsConsumed;
    }

    public BigDecimal getPricePerUnit() {
        return pricePerUnit;
    }

    public void setPricePerUnit(BigDecimal pricePerUnit) {
        this.pricePerUnit = pricePerUnit;
    }

    public BigDecimal getAmountDue() {
        return amountDue;
    }

    public void setAmountDue(BigDecimal amountDue) {
        this.amountDue = amountDue;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public boolean isPaid() {
        return isPaid;
    }

    public void setPaid(boolean paid) {
        isPaid = paid;
    }

    @Override
    public String toString() {
        return "Bill{" +
                "id=" + id +
                ", customerId=" + customerId +
                ", billNumber='" + billNumber + '\'' +
                ", amountDue=" + amountDue +
                ", isPaid=" + isPaid +
                '}';
    }
}