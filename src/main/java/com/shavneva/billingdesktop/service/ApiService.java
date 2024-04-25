package com.shavneva.billingdesktop.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shavneva.billingdesktop.entity.User;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Base64;
import java.util.function.Consumer;

public class ApiService {
    public static final String BASE_URL = "http://localhost:8080/api";

    public static void authenticateUser(String userName, String password, Consumer<Boolean> callback) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL+"/users/getAll"))
                .header("Authorization", "Basic "+ Base64.getEncoder().encodeToString((userName+":"+ password).getBytes()))
                .build();

        HttpClient.newHttpClient().sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::statusCode)
                .thenAccept(statusCode -> {
                    if (statusCode == 200) {
                        callback.accept(true);
                    } else {
                        callback.accept(false);
                    }
                });
    }

    public static void registerUser(String firstName, String lastName, String email, String phoneNumber,
                                    String password, Consumer<Boolean> callback)  {

        User user = new User(firstName, lastName, email, phoneNumber, password);
        ObjectMapper mapper = new ObjectMapper();
        String userJson = null;
        try {
            userJson = mapper.writeValueAsString(user);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/users/create"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString( userJson))
                .build();

        HttpClient.newHttpClient().sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(responseBody -> {
                    if (responseBody.equals("success")) {
                        callback.accept(true);
                    } else {
                        callback.accept(false);
                    }
                });
    }
}
