package com.pahanaedu.dao;

import com.pahanaedu.models.User;
import java.util.Optional;

public interface IUserDAO {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email); // Good to prevent duplicate emails
    User createUser(User user);
}