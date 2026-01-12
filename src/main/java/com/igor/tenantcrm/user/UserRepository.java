package com.igor.tenantcrm.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<AppUser, UUID> {
    Optional<AppUser> findByTenant_IdAndEmail(UUID tenantId, String email);
}

