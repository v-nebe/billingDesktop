package com.shavneva.billingdesktop.service;

import com.shavneva.billingdesktop.entity.User;
import com.shavneva.billingdesktop.repository.AddAuthHeadersRequestFilter;
import com.shavneva.billingdesktop.repository.CrudRepository;
import com.shavneva.billingdesktop.repository.SecurityContext;
import com.shavneva.billingdesktop.repository.factory.CrudFactory;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
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

    public static void registerUser(String firstName, String lastName, String email, String phoneNumber,
                                    String password, Consumer<Boolean> callback) {

        User user = new User(firstName, lastName, email, phoneNumber, password);
        CrudRepository<User> userRepository = CrudFactory.createUserRepository();
        userRepository.create(user);
        callback.accept(true);

 /*       try {
            userRepository.create(user);
            callback.accept(true);
        } catch (Exception e) {
            callback.accept(false);
        }*/
    }
}
