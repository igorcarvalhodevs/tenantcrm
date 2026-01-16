package com.igor.tenantcrm.security;

import java.util.UUID;

public record AuthenticatedUser(
        UUID userId,
        UUID tenantId,
        String role
) {}

