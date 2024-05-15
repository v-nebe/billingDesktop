package com.shavneva.billingdesktop.entity;

public class InfoContext {
    private static User currentUser;
    private static Role role;

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

}