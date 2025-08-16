package com.pahanaedu.models;

public class Administrator extends User {

    public Administrator() {
        super();
        this.setRole(Role.ADMIN);
    }

    public Administrator(String username, String passwordHash, String fullName, String email) {
        super(username, passwordHash, fullName, email, Role.ADMIN);
    }

}