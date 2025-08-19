package com.pahanaedu.clients;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pahanaedu.api.dto.CustomerDTO;
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

public class CustomerApiClient {

    private static final String API_BASE_URL = "http://localhost:8081/pahana-edu-api/api";
    private final HttpClient httpClient;
    private final Gson gson;

    public CustomerApiClient() {
        this.httpClient = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .connectTimeout(Duration.ofSeconds(10))
                .build();
        this.gson = new Gson();
    }

    public List<CustomerDTO> getAllCustomers() throws IOException, InterruptedException, ApiClientException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_BASE_URL + "/customers"))
                .header("Accept", "application/json")
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            Type dtoListType = new TypeToken<List<CustomerDTO>>() {}.getType();
            return gson.fromJson(response.body(), dtoListType);
        } else {
            throw new ApiClientException("Failed to fetch customers: " + response.body(), response.statusCode());
        }
    }

    public Optional<CustomerDTO> getCustomerById(long id) throws IOException, InterruptedException, ApiClientException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_BASE_URL + "/customers/" + id))
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            return Optional.of(gson.fromJson(response.body(), CustomerDTO.class));
        } else if (response.statusCode() == 404) {
            return Optional.empty();
        } else {
            throw new ApiClientException("Failed to fetch customer by ID: " + response.body(), response.statusCode());
        }
    }

}