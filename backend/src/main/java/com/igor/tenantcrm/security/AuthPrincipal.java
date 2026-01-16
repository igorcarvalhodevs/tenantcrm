package com.igor.tenantcrm.security;

import java.util.UUID;

public record AuthPrincipal(
        UUID userId,
        UUID tenantId,
        String role
) {}

