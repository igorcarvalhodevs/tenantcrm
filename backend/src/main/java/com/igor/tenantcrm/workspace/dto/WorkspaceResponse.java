package com.igor.tenantcrm.workspace.dto;

import com.igor.tenantcrm.workspace.Workspace;

import java.time.Instant;
import java.util.UUID;

public record WorkspaceResponse(
        UUID id,
        UUID tenantId,
        String name,
        Instant createdAt
) {
    public static WorkspaceResponse from(Workspace w) {
        return new WorkspaceResponse(
                w.getId(),
                w.getTenantId(),
                w.getName(),
                w.getCreatedAt()
        );
    }
}



