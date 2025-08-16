package com.pahanaedu.services;

import com.pahanaedu.exceptions.AuthenticationException;
import com.pahanaedu.exceptions.RegistrationException;
import com.pahanaedu.models.User;

public interface IAuthService {
    User login(String username, String plainPassword) throws AuthenticationException;
    User registerUser(String username, String plainPassword, String fullName, String email) throws RegistrationException;
}