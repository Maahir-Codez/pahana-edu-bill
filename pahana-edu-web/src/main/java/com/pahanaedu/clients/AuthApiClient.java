package com.pahanaedu.clients;

import com.google.gson.Gson;
import com.pahanaedu.api.dto.LoginRequestDTO;
import com.pahanaedu.api.dto.UserDTO;
import com.pahanaedu.exceptions.ApiClientException;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Map;

public class AuthApiClient {

    private static final String API_BASE_URL = "http://localhost:8081/pahana-edu-api/api";

    private final HttpClient httpClient;
    private final Gson gson;

    public AuthApiClient() {
        this.httpClient = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .connectTimeout(Duration.ofSeconds(10))
                .build();
        this.gson = new Gson();
    }

    public UserDTO login(String username, String password) throws IOException, InterruptedException, ApiClientException {
        LoginRequestDTO loginRequestDTO = new LoginRequestDTO();
        loginRequestDTO.setUsername(username);
        loginRequestDTO.setPassword(password);

        String requestBody = gson.toJson(loginRequestDTO);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_BASE_URL + "/auth/login"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            return gson.fromJson(response.body(), UserDTO.class);
        } else {
            String errorMessage = "Login failed with status code: " + response.statusCode();
            try {
                Map<String, String> errorResponse = gson.fromJson(response.body(), Map.class);
                if (errorResponse != null && errorResponse.containsKey("error")) {
                    errorMessage = errorResponse.get("error");
                }
            } catch (Exception e) {
                errorMessage = response.body();
            }
            throw new ApiClientException(errorMessage, response.statusCode());
        }
    }
}