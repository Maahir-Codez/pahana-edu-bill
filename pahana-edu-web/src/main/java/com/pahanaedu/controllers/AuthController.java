package com.pahanaedu.controllers;

import com.pahanaedu.api.dto.UserDTO; // We now use the DTO
import com.pahanaedu.clients.AuthApiClient;
import com.pahanaedu.exceptions.ApiClientException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/app/auth/*")
public class AuthController extends HttpServlet {

    private AuthApiClient authApiClient;

    @Override
    public void init() {
        this.authApiClient = new AuthApiClient();
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
            default:
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getPathInfo();
        if ("/login".equals(action)) {
            handleLogin(request, response);
        } else {
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
            UserDTO userDTO = authApiClient.login(username, password);

            HttpSession session = request.getSession();
            session.setAttribute("loggedInUser", userDTO);

            response.sendRedirect(request.getContextPath() + "/app/dashboard");

        } catch (ApiClientException e) {
            request.setAttribute("errorMessage", e.getMessage());
            showLoginPage(request, response);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Error connecting to the authentication service. Please try again later.");
            showLoginPage(request, response);
        }
    }

    private void logout(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        response.sendRedirect(request.getContextPath() + "/app/auth/login?status=logout_success");
    }
}