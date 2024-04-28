package com.shavneva.billingdesktop.entity;

import lombok.*;
import java.math.BigDecimal;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Account {

    private Integer moneyId;
    private BigDecimal amount;
}