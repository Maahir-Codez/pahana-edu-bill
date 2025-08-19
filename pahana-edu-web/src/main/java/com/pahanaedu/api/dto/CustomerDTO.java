package com.pahanaedu.api.dto;

public class CustomerDTO {
    private Long id;
    private String accountNumber;
    private String fullName;
    private String address;
    private String city;
    private String postalCode;
    private String phoneNumber;
    private String email;
    private boolean active;
    private String dateRegistered;

    public CustomerDTO() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getAccountNumber() { return accountNumber; }
    public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
    public String getPostalCode() { return postalCode; }
    public void setPostalCode(String postalCode) { this.postalCode = postalCode; }
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public boolean getActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
    public String getDateRegistered() { return dateRegistered; }
    public void setDateRegistered(String dateRegistered) { this.dateRegistered = dateRegistered; }
}