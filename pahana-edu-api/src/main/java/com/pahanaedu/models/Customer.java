package com.pahanaedu.models;

import java.time.LocalDateTime;

public class Customer {

    private Long id;
    private String accountNumber;
    private String fullName;
    private String address;
    private String city;
    private String postalCode;
    private String phoneNumber;
    private String email;
    private LocalDateTime dateRegistered;
    private boolean isActive;
    private double unitsConsumed;


    public Customer() {
    }

    public Customer(String accountNumber, String fullName, String address, String city, String postalCode, String phoneNumber, String email) {
        this.accountNumber = accountNumber;
        this.fullName = fullName;
        this.address = address;
        this.city = city;
        this.postalCode = postalCode;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.dateRegistered = LocalDateTime.now();
        this.isActive = true;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDateTime getDateRegistered() {
        return dateRegistered;
    }

    public void setDateRegistered(LocalDateTime dateRegistered) {
        this.dateRegistered = dateRegistered;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public double getUnitsConsumed() {
        return unitsConsumed;
    }

    public void setUnitsConsumed(double unitsConsumed) {
        this.unitsConsumed = unitsConsumed;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", accountNumber='" + accountNumber + '\'' +
                ", fullName='" + fullName + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}