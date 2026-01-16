package com.igor.tenantcrm.security;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(JwtAuthFilter.class);

    private final JwtService jwtService;

    public JwtAuthFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();
        return path != null && path.startsWith("/auth/");
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String header = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (header == null || !header.startsWith("Bearer ")) {
            log.debug("[JWT] no bearer token on path={}", request.getServletPath());
            filterChain.doFilter(request, response);
            return;
        }

        String token = header.substring(7);

        try {
            Claims claims = jwtService.parse(token);

            String userId = claims.getSubject();
            String tenantId = claims.get("tenantId", String.class);
            String role = claims.get("role", String.class);

            log.debug("[JWT] valid token userId={}, tenantId={}, role={}, path={}",
                    userId, tenantId, role, request.getServletPath());

            var auth = new UsernamePasswordAuthenticationToken(
                    userId,
                    null,
                    List.of(new SimpleGrantedAuthority("ROLE_" + role))
            );

            // Guardar tenantId para o controller/service usar
            auth.setDetails(Map.of(
                    "tenantId", tenantId,
                    "role", role
            ));

            SecurityContextHolder.getContext().setAuthentication(auth);

        } catch (Exception ex) {
            log.warn("[JWT] invalid token on path={} cause={}", request.getServletPath(), ex.toString());
            SecurityContextHolder.clearContext();
        }

        filterChain.doFilter(request, response);
    }
}





