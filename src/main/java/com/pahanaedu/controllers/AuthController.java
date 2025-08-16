package com.pahanaedu.controllers;

import com.pahanaedu.exceptions.AuthenticationException;
import com.pahanaedu.exceptions.RegistrationException;
import com.pahanaedu.models.User;
import com.pahanaedu.services.AuthService;
import com.pahanaedu.services.IAuthService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/auth/*")
public class AuthController extends HttpServlet {
    private IAuthService authService;

    @Override
    public void init() throws ServletException {
        this.authService = new AuthService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getPathInfo();
        if (action == null) {
            action = "/login";
        }

        switch (action) {
            case "/login":
                showLoginPage(request, response);
                break;
            case "/logout":
                logout(request, response);
                break;
            case "/register": // Add this case
                showRegisterPage(request, response);
                break;
            default:
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getPathInfo();
        if (action == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        switch (action) {
            case "/login":
                handleLogin(request, response);
                break;
            case "/register": // Add this case
                handleRegister(request, response);
                break;
            default:
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private void showLoginPage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/views/login.jsp").forward(request, response);
    }

    private void handleLogin(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        try {
            User user = authService.login(username, password);

            HttpSession session = request.getSession();
            session.setAttribute("loggedInUser", user);

            response.sendRedirect(request.getContextPath() + "/dashboard");

        } catch (AuthenticationException e) {
            request.setAttribute("errorMessage", e.getMessage());
            showLoginPage(request, response);
        }
    }

    private void logout(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        response.sendRedirect(request.getContextPath() + "/auth/login?status=logout_success");
    }

    private void showRegisterPage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/views/register.jsp").forward(request, response);
    }

    private void handleRegister(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String fullName = request.getParameter("fullName");
        String email = request.getParameter("email");
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        try {
            authService.registerUser(username, password, fullName, email);
            // On success, redirect to the login page with a success message
            response.sendRedirect(request.getContextPath() + "/auth/login?status=reg_success");
        } catch (RegistrationException e) {
            // On failure, forward back to the register page with an error
            request.setAttribute("errorMessage", e.getMessage());
            showRegisterPage(request, response);
        }
    }
}