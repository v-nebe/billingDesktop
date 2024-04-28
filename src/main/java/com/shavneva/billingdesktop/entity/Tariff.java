package com.shavneva.billingdesktop.entity;

import lombok.*;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Tariff{

    private Integer tariffId;
    private String tariffName;
    private String price;
}
