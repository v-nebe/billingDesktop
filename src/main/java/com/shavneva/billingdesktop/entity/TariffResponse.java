package com.shavneva.billingdesktop.entity;

import lombok.Data;

import java.util.List;

@Data
public class TariffResponse {
    private List<Tariff> tariffs;

}