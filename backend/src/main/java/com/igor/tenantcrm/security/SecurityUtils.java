package com.igor.tenantcrm.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public final class SecurityUtils {

    private SecurityUtils() {}

    public static AuthenticatedUser currentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || auth.getPrincipal() == null) {
            throw new IllegalStateException("No authenticated user in SecurityContext");
        }

        if (!(auth.getPrincipal() instanceof AuthenticatedUser user)) {
            throw new IllegalStateException("SecurityContext principal is not AuthenticatedUser");
        }

        return user;
    }
}

