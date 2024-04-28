package com.shavneva.billingdesktop.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shavneva.billingdesktop.DAO.UserDao;
import com.shavneva.billingdesktop.entity.User;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Base64;
import java.util.function.Consumer;

public class ApiService {
    public static final String BASE_URL = "http://localhost:8080/api";

    public static void authenticateUser(String userName, String password, Consumer<Boolean> callback) {
        Client client = ClientBuilder.newClient();

        String credentials = userName + ":" + password;
        String base64Credentials = java.util.Base64.getEncoder().encodeToString(credentials.getBytes());

        Response response = client.target(BASE_URL + "/users/getAll")
                .request()
                .header("Authorization", "Basic " + base64Credentials)
                .get();

        int status = response.getStatus();
        if (status == 200) {
            callback.accept(true);
        } else {
            callback.accept(false);
        }

        response.close();
        client.close();
    }

    public static void registerUser(String firstName, String lastName, String email, String phoneNumber,
                                    String password, Consumer<Boolean> callback) {
        Client client = ClientBuilder.newClient();

        User user = new User(firstName, lastName, email, phoneNumber, password);
        Response response = client.target(BASE_URL + "/users/create")
                .request()
                .post(Entity.json(user));

        int status = response.getStatus();
        if (status == 200) {
            callback.accept(true);
        } else {
            callback.accept(false);
        }

        response.close();
        client.close();
    }
}
