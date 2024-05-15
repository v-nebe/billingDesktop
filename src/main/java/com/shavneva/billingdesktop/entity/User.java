package com.shavneva.billingdesktop.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

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

    private Integer tariffId;
    private Integer accountId;
}