package com.igor.tenantcrm.security;

import java.util.UUID;

public record JwtPrincipal(
        UUID userId,
        UUID tenantId,
        String role
) {}

