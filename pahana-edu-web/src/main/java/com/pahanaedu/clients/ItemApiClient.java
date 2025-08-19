package com.pahanaedu.clients;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pahanaedu.api.dto.ItemDTO;
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
import java.util.Optional;

public class ItemApiClient {

    private static final String API_BASE_URL = "http://localhost:8081/pahana-edu-api/api";
    private final HttpClient httpClient;
    private final Gson gson;

    public ItemApiClient() {
        this.httpClient = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .connectTimeout(Duration.ofSeconds(10))
                .build();
        this.gson = new Gson();
    }

    public List<ItemDTO> getAllItems() throws IOException, InterruptedException, ApiClientException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_BASE_URL + "/items"))
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            Type dtoListType = new TypeToken<List<ItemDTO>>() {}.getType();
            return gson.fromJson(response.body(), dtoListType);
        } else {
            handleErrorResponse(response);
            return null;
        }
    }

    public Optional<ItemDTO> getItemById(long id) throws IOException, InterruptedException, ApiClientException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_BASE_URL + "/items/" + id))
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            return Optional.of(gson.fromJson(response.body(), ItemDTO.class));
        } else if (response.statusCode() == 404) {
            return Optional.empty();
        } else {
            handleErrorResponse(response);
            return Optional.empty();
        }
    }

    public ItemDTO createItem(ItemDTO newItemDTO) throws IOException, InterruptedException, ApiClientException {
        String requestBody = gson.toJson(newItemDTO);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_BASE_URL + "/items"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 201) {
            return gson.fromJson(response.body(), ItemDTO.class);
        } else {
            handleErrorResponse(response);
            return null;
        }
    }

    public void updateItem(ItemDTO itemDTO) throws IOException, InterruptedException, ApiClientException {
        String requestBody = gson.toJson(itemDTO);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_BASE_URL + "/items/" + itemDTO.getId()))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            handleErrorResponse(response);
        }
    }

    public void deleteItem(long itemId) throws IOException, InterruptedException, ApiClientException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_BASE_URL + "/items/" + itemId))
                .DELETE()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 204) {
            handleErrorResponse(response);
        }
    }

    private void handleErrorResponse(HttpResponse<String> response) throws ApiClientException {
        String errorMessage = "Request failed with status code: " + response.statusCode();
        try {
            Map<String, String> errorResponse = gson.fromJson(response.body(), Map.class);
            if (errorResponse != null && errorResponse.containsKey("error")) {
                errorMessage = errorResponse.get("error");
            }
        } catch (Exception e) {
            if (response.body() != null && !response.body().isEmpty()) {
                errorMessage = response.body();
            }
        }
        throw new ApiClientException(errorMessage, response.statusCode());
    }
}