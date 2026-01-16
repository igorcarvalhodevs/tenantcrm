package com.igor.tenantcrm.auth;

import com.igor.tenantcrm.auth.dto.AuthRequest;
import com.igor.tenantcrm.auth.dto.AuthResponse;
import com.igor.tenantcrm.security.JwtService;
import com.igor.tenantcrm.tenant.Tenant;
import com.igor.tenantcrm.tenant.TenantRepository;
import com.igor.tenantcrm.user.AppUser;
import com.igor.tenantcrm.user.UserRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthService.class);

    private final TenantRepository tenantRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(
            TenantRepository tenantRepository,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService
    ) {
        this.tenantRepository = tenantRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public AuthResponse login(AuthRequest req) {
        log.info("[LOGIN] attempt: tenantSlug='{}', email='{}'", req.tenantSlug(), req.email());

        Tenant tenant = tenantRepository.findBySlug(req.tenantSlug())
                .filter(Tenant::isActive)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid tenant"));

        log.info("[LOGIN] tenant found: id={}, slug='{}', active={}", tenant.getId(), tenant.getSlug(), tenant.isActive());

        AppUser user = userRepository.findByTenantIdAndEmail(tenant.getId(), req.email())
                .filter(AppUser::isActive)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials"));

        log.info("[LOGIN] user found: id={}, email='{}', role={}, active={}", user.getId(), user.getEmail(), user.getRole(), user.isActive());

        boolean matches = passwordEncoder.matches(req.password(), user.getPasswordHash());
        log.info("[LOGIN] password matches? {}", matches);

        if (!matches) {
            log.warn("[LOGIN] invalid password for email='{}'", user.getEmail());
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
        }

        String token = jwtService.generate(user.getId(), tenant.getId(), user.getRole().name());
        return new AuthResponse("Bearer", token);
    }
}





