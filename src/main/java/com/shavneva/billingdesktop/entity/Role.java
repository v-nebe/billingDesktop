package com.shavneva.billingdesktop.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Role {
    private Long roleId;
    private String roleName;

    public static final Role ADMIN = Role.builder().roleId(1L).roleName("ROLE_ADMIN").build();
    public static final Role USER = Role.builder().roleId(2L).roleName("ROLE_USER").build();
}
