package com.shavneva.billingdesktop.entity;

import lombok.*;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Services{
    private Integer serviceId;
    private String service;
}
