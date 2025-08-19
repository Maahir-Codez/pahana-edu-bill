package com.pahanaedu.api.servlets;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pahanaedu.api.dto.LoginRequestDTO;
import com.pahanaedu.api.dto.UserDTO;
import com.pahanaedu.api.mappers.UserMapper;
import com.pahanaedu.exceptions.AuthenticationException;
import com.pahanaedu.models.User;
import com.pahanaedu.services.AuthService;
import com.pahanaedu.services.IAuthService;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/api/auth/*")
public class AuthApiServlet extends HttpServlet {

    private IAuthService authService;
    private Gson gson;

    @Override
    public void init() {
        this.authService = new AuthService();
        this.gson = new GsonBuilder().setPrettyPrinting().create();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String pathInfo = req.getPathInfo();
        if ("/login".equals(pathInfo)) {
            handleLogin(req, resp);
        } else {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private void handleLogin(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        try {
            LoginRequestDTO loginRequest = gson.fromJson(req.getReader(), LoginRequestDTO.class);
            if (loginRequest == null) {
                throw new AuthenticationException("Invalid login request payload.");
            }

            User authenticatedUser = authService.login(loginRequest.getUsername(), loginRequest.getPassword());

            UserDTO userDTO = UserMapper.toDTO(authenticatedUser);

            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write(gson.toJson(userDTO));

        } catch (AuthenticationException e) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401 Unauthorized
            resp.getWriter().write(gson.toJson(new ErrorResponse(e.getMessage())));
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write(gson.toJson(new ErrorResponse("An unexpected server error occurred.")));
            e.printStackTrace();
        }
    }

    private static class ErrorResponse {
        private final String error;
        public ErrorResponse(String error) { this.error = error; }
    }
}