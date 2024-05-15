package com.shavneva.billingdesktop.entity;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Tariff{

    private Integer tariffId;
    private String tariffName;
    private BigDecimal price;

    private List<Services> services;
}
