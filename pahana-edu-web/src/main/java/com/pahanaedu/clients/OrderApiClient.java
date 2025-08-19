package com.pahanaedu.clients;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pahanaedu.api.dto.OrderDTO;
import com.pahanaedu.api.dto.OrderRequestDTO;
import com.pahanaedu.exceptions.ApiClientException;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;
import java.util.Map;

public class OrderApiClient extends BaseApiClient {
    private static final String API_BASE_URL = "http://localhost:8081/pahana-edu-api/api";
    private final HttpClient httpClient;
    private final Gson gson;

    public OrderApiClient() {
        this.httpClient = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(10)).build();
        this.gson = new Gson();
    }

    public Map<String, Object> createOrder(OrderRequestDTO orderRequest) throws IOException, InterruptedException, ApiClientException {
        String requestBody = gson.toJson(orderRequest);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_BASE_URL + "/orders"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 201) {
            return gson.fromJson(response.body(), Map.class);
        } else {
            String errorMessage = "Failed to create order. Status: " + response.statusCode();
            try {
                Map<String, String> errorMap = gson.fromJson(response.body(), Map.class);
                if (errorMap != null && errorMap.containsKey("error")) {
                    errorMessage = errorMap.get("error");
                }
            } catch (Exception e) { /* Ignore parsing error */ }
            throw new ApiClientException(errorMessage, response.statusCode());
        }
    }

    public List<OrderDTO> getAllOrders() throws IOException, InterruptedException, ApiClientException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_BASE_URL + "/orders"))
                .GET().build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            Type dtoListType = new TypeToken<List<OrderDTO>>() {}.getType();
            return gson.fromJson(response.body(), dtoListType);
        } else {
            handleErrorResponse(response);
            return null;
        }
    }

    public OrderDTO getOrderById(long id) throws IOException, InterruptedException, ApiClientException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_BASE_URL + "/orders/" + id))
                .GET().build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 200) {
            return gson.fromJson(response.body(), OrderDTO.class);
        } else {
            handleErrorResponse(response);
            return null;
        }
    }

}