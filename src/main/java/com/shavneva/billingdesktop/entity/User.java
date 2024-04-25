package com.shavneva.billingdesktop.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    private String firstName;

    private String lastName;

    private String email;

    private String number;

    private String password;

}