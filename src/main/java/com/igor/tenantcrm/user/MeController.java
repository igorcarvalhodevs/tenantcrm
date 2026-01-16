package com.igor.tenantcrm.user;

import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MeController {

    @GetMapping("/me")
    public Map<String, Object> me(Authentication auth) {
        return Map.of(
            "authenticated", auth != null && auth.isAuthenticated(),
            "principal", auth != null ? auth.getName() : null,
            "authorities", auth != null ? auth.getAuthorities() : null
        );
    }
}


