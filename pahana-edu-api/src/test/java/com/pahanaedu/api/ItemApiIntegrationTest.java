package com.pahanaedu.api;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.pahanaedu.api.dto.ItemDTO;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ItemApiIntegrationTest {

    private static HttpClient httpClient;
    private static final String API_BASE_URL = "http://localhost:8081/pahana-edu-api/api/items";

    @BeforeAll
    static void setup() {
        // Create a single HttpClient for all tests in this class
        httpClient = HttpClient.newHttpClient();
    }

    @Test
    @DisplayName("GET /api/items should return 200 OK and a list of items")
    void getAllItems_shouldReturnOkAndItemList() throws IOException, InterruptedException {
        // Arrange
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_BASE_URL))
                .GET()
                .build();

        // Act
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        // Assert
        assertEquals(200, response.statusCode(), "The HTTP status code should be 200 OK.");

        // Assert that the body is valid JSON and can be parsed into a list of ItemDTOs
        assertDoesNotThrow(() -> {
            Type dtoListType = new TypeToken<List<ItemDTO>>() {}.getType();
            List<ItemDTO> items = new Gson().fromJson(response.body(), dtoListType);
            assertNotNull(items, "The parsed item list should not be null.");
            // You could add more assertions here, e.g., assertTrue(!items.isEmpty());
        }, "Response body should be a valid JSON array of items.");
    }

    @Test
    @DisplayName("GET /api/items/{id} with a non-existent ID should return 404 Not Found")
    void getItemById_withNonExistentId_shouldReturnNotFound() throws IOException, InterruptedException {
        // Arrange
        long nonExistentId = 99999L;
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_BASE_URL + "/" + nonExistentId))
                .GET()
                .build();

        // Act
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        // Assert
        assertEquals(404, response.statusCode(), "The HTTP status code should be 404 Not Found.");
    }
}