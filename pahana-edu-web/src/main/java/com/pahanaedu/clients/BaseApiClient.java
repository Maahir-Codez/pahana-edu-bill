package com.pahanaedu.clients;

import com.google.gson.Gson;
import com.pahanaedu.exceptions.ApiClientException;

import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.util.Map;

public abstract class BaseApiClient {
    protected static final String API_BASE_URL = "http://localhost:8081/pahana-edu-api/api";
    protected final HttpClient httpClient;
    protected final Gson gson;

    public BaseApiClient() {
        this.httpClient = HttpClient.newHttpClient();
        this.gson = new Gson();
    }

    protected void handleErrorResponse(HttpResponse<String> response) throws ApiClientException {
        String errorMessage = "Request failed with status code: " + response.statusCode();
        try {
            // Try to parse the standard error format: {"error": "message"}
            Map<String, String> errorResponse = gson.fromJson(response.body(), Map.class);
            if (errorResponse != null && errorResponse.containsKey("error")) {
                errorMessage = errorResponse.get("error");
            }
        } catch (Exception e) {
            // If JSON parsing fails, use the raw response body if it's not empty
            if (response.body() != null && !response.body().isEmpty()) {
                errorMessage = response.body();
            }
        }
        throw new ApiClientException(errorMessage, response.statusCode());
    }
}