package com.shavneva.billingdesktop.entity;

import lombok.Getter;
import lombok.Setter;

public class InfoContext {
    @Setter
    @Getter
    private static User currentUser;
    @Setter
    @Getter
    private static Role role;
    @Setter
    @Getter
    private  static Tariff tariff;
    @Setter
    @Getter
    private static Account amount;

}