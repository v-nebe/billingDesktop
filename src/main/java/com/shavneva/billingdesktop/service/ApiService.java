package com.shavneva.billingdesktop.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.shavneva.billingdesktop.entity.Role;
import com.shavneva.billingdesktop.entity.Tariff;
import com.shavneva.billingdesktop.entity.User;
import com.shavneva.billingdesktop.repository.CrudRepository;
import com.shavneva.billingdesktop.repository.SecurityContext;
import com.shavneva.billingdesktop.repository.factory.CrudFactory;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import com.google.gson.JsonObject;

public class ApiService {
    public static final String BASE_URL = "http://localhost:8080/api";

    public static void authenticateUser(String userName, String password, Consumer<Boolean> callback) {

        SecurityContext.setUsername(userName);
        SecurityContext.setPassword(password);

        CrudRepository<User> userRepository = CrudFactory.createUserRepository();
        List<User> users = userRepository.getAll();

        if (!users.isEmpty()) {
            callback.accept(true);
        } else {
            callback.accept(false);
        }

    }

    public static void registerUser(String firstName, String lastName, String email, String phoneNumber,
                                    String password, Consumer<Boolean> callback) {

        User user = new User(null, firstName, lastName, email, phoneNumber, password, null, null,
                null, null);
        CrudRepository<User> userRepository = CrudFactory.createUserRepository();
        userRepository.create(user);
        callback.accept(true);
    }

    public static void getAllUsers(Consumer<List<User>> callback) {
        CrudRepository<User> userRepository = CrudFactory.createUserRepository();
        List<User> users = userRepository.getAll();
        callback.accept(users);
    }

    public static void getTariffWithServices(Consumer<List<Tariff>> callback) {
        String url = BASE_URL + "/tariffs/tariffs-with-services";

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();

        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(responseBody -> {
                    try {
                        ObjectMapper objectMapper = new ObjectMapper();
                        List<Tariff> tariffs = objectMapper.readValue(responseBody, new TypeReference<List<Tariff>>() {});
                        callback.accept(tariffs);
                    } catch (IOException e) {
                        e.printStackTrace();
                        callback.accept(Collections.emptyList());
                    }
                })
                .exceptionally(e -> {
                    System.err.println("Error occurred: " + e);
                    callback.accept(Collections.emptyList());
                    return null;
                });
    }


    public static void depositMoney(String email, BigDecimal amount) {
        String url = BASE_URL + "/billing/deposit";

        try {
            JsonObject requestBody = new JsonObject();
            requestBody.addProperty("email", email);
            requestBody.addProperty("amount", amount);

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody.toString()))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                System.out.println("Money deposited successfully.");
            } else {
                System.out.println("Error depositing money: " + response.body());
            }
        } catch (IOException | InterruptedException e) {
            System.out.println("Error depositing money: " + e.getMessage());
        }
    }

    public static void getNotificationType(String userEmail, Consumer<String> callback) {
        String url = BASE_URL + "/notification/get_type?userEmail=" + URLEncoder.encode(userEmail, StandardCharsets.UTF_8);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();

        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(callback)
                .exceptionally(e -> {
                    System.err.println("Error occurred: " + e);
                    return null;
                });
    }

    // Метод для сохранения нового типа уведомлений
    public static void saveNotificationType(String userEmail, String newNotificationType, Runnable callback) {
        String url = BASE_URL + "/notification/save_type?userEmail=" + URLEncoder.encode(userEmail, StandardCharsets.UTF_8);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .POST(HttpRequest.BodyPublishers.ofString(newNotificationType))
                .build();

        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenAccept(response -> {
                    if (response.statusCode() == 200) {
                        callback.run();
                    } else {
                        System.err.println("Error: " + response.statusCode());
                    }
                })
                .exceptionally(e -> {
                    System.err.println("Error occurred: " + e);
                    return null;
                });
    }

}
