package com.pahanaedu.services;

import com.pahanaedu.dao.IUserDAO;
import com.pahanaedu.dao.UserDAO;
import com.pahanaedu.exceptions.AuthenticationException;
import com.pahanaedu.exceptions.RegistrationException;
import com.pahanaedu.models.Role;
import com.pahanaedu.models.User;
import com.pahanaedu.utils.PasswordUtil;

import java.util.Optional;

public class AuthService implements IAuthService {

    private final IUserDAO userDAO;

    public AuthService(IUserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public AuthService() {
        this(new UserDAO());
    }

    @Override
    public User login(String username, String plainPassword) throws AuthenticationException {
        if (username == null || username.trim().isEmpty() || plainPassword == null || plainPassword.isEmpty()) {
            throw new AuthenticationException("Username and password cannot be empty.");
        }

        Optional<User> userOptional = userDAO.findByUsername(username);

        if (!userOptional.isPresent()) {
            throw new AuthenticationException("Invalid username or password.");
        }

        User user = userOptional.get();

        boolean passwordMatches = PasswordUtil.verifyPassword(plainPassword, user.getPasswordHash());

        if (!passwordMatches) {
            throw new AuthenticationException("Invalid username or password.");
        }

        if (!user.isActive()) {
            throw new AuthenticationException("This user account has been deactivated.");
        }

        System.out.println("User '" + user.getUsername() + "' logged in successfully.");
        return user;
    }

    @Override
    public User registerUser(String username, String plainPassword, String fullName, String email) throws RegistrationException {
        // 1. Validation
        if (username == null || username.trim().length() < 3) {
            throw new RegistrationException("Username must be at least 3 characters long.");
        }
        if (plainPassword == null || plainPassword.length() < 6) {
            throw new RegistrationException("Password must be at least 6 characters long.");
        }
        // A simple email regex check
        if (email == null || !email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            throw new RegistrationException("Please enter a valid email address.");
        }
        if (fullName == null || fullName.trim().isEmpty()) {
            throw new RegistrationException("Full name cannot be empty.");
        }

        // 2. Check for uniqueness
        if (userDAO.findByUsername(username).isPresent()) {
            throw new RegistrationException("Username is already taken.");
        }
        if (userDAO.findByEmail(email).isPresent()) {
            throw new RegistrationException("Email is already registered.");
        }

        // 3. Hash the password
        String passwordHash = PasswordUtil.hashPassword(plainPassword);

        // 4. Create a new User object (defaulting new users to STAFF role)
        User newUser = new User(username, passwordHash, fullName, email, Role.STAFF);

        // 5. Save the user using the DAO
        return userDAO.createUser(newUser);
    }
}