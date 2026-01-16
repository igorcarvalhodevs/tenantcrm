package com.igor.tenantcrm.auth.dto;

public record AuthResponse(
        String tokenType,
        String token
) {}


