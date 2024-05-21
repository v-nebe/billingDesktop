package com.shavneva.billingdesktop.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class User {

    private Integer userId;
    private String firstName;
    private String lastName;
    private String email;
    private String number;
    private String password;
    private String notificationType;

    private Tariff tariff;
    private Account account;
    private List<Role> roles;

    public User(Object id, String firstName, String lastName, String email, String phoneNumber,
                String password, Object tariff, Object account) {
    }
    public User(Object id, String firstName, String lastName, String email, String phoneNumber,
                String password, Object tariff, Object account, Object role) {
    }
}