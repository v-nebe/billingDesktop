package com.shavneva.billingdesktop.repository;

public class SecurityContext {
    private static String username = null;
    private static String password;

    public static String getUsername() {
        return username;
    }

    public static void setUsername(String username) {
        SecurityContext.username = username;
    }

    public static String getPassword() {
        return password;
    }

    public static void setPassword(String password) {
        SecurityContext.password = password;
    }
}