package com.pahanaedu.controllers;

import com.pahanaedu.api.dto.RegisterRequestDTO;
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
            case "/register":
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
            case "/register":
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

    private void handleRegister(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String fullName = request.getParameter("fullName");
        String email = request.getParameter("email");
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        try {
            authApiClient.register(username, password, fullName, email);

            response.sendRedirect(request.getContextPath() + "/app/auth/login?status=reg_success");

        } catch (ApiClientException e) {
            request.setAttribute("errorMessage", e.getMessage());

            RegisterRequestDTO dto = new RegisterRequestDTO();
            dto.setFullName(fullName);
            dto.setEmail(email);
            dto.setUsername(username);
            request.setAttribute("formData", dto);

            showRegisterPage(request, response);

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Error connecting to the registration service. Please try again later.");
            showRegisterPage(request, response);
        }
    }

    private void showRegisterPage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/views/register.jsp").forward(request, response);
    }

    private void logout(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        response.sendRedirect(request.getContextPath() + "/app/auth/login?status=logout_success");
    }
}