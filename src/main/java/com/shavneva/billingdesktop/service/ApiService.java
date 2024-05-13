package com.shavneva.billingdesktop.service;

import com.shavneva.billingdesktop.entity.Role;
import com.shavneva.billingdesktop.entity.User;
import com.shavneva.billingdesktop.repository.CrudRepository;
import com.shavneva.billingdesktop.repository.SecurityContext;
import com.shavneva.billingdesktop.repository.factory.CrudFactory;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.function.Consumer;

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

    public static void getUserInfo(String userName, Consumer<User> callback) {
        CrudRepository<User> userCrudRepository = CrudFactory.createUserRepository();
        List<User> users = userCrudRepository.getAll();
        for (User user : users) {
            if (user.getEmail().equals(userName)) {
                callback.accept(user);
                return;
            }
        }
        callback.accept(null);
    }

    public static void registerUser(String firstName, String lastName, String email, String phoneNumber,
                                    String password, Consumer<Boolean> callback) {

        User user = new User(firstName, lastName, email, phoneNumber, password, null, null);
        CrudRepository<User> userRepository = CrudFactory.createUserRepository();
        userRepository.create(user);
        callback.accept(true);
    }

    public static Role getRoleName() {
        return  null;
    }

    public static void depositMoney(String email, BigDecimal amount) {
        String url = BASE_URL + "/billing/deposit?email=" + email + "&amount=" + amount;

        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
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
}
