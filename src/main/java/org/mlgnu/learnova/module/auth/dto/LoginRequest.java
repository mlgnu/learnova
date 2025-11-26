package org.mlgnu.learnova.module.auth.dto;

public record LoginRequest(
        String username,
        String password
) {}
