package com.igor.tenantcrm.auth.dto;

public record AuthRequest(
        String tenantSlug,
        String email,
        String password
) {}


