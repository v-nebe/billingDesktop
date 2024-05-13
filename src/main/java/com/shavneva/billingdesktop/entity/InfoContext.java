package com.shavneva.billingdesktop.entity;

public class InfoContext {
    private static User currentUser;
    private static Role role;

    private static Tariff tariffs;

    public static User getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(User user) {
        currentUser = user;
    }

    public static Role getRole() {
        return role;
    }

    public static void setRole(Role userRole) {
        role = userRole;
    }

    public static Tariff getTariffs() {
        return tariffs;
    }

    public static void setTariffs(Tariff tariffs) {
        InfoContext.tariffs = tariffs;
    }
}