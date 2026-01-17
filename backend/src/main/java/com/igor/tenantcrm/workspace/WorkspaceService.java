package com.igor.tenantcrm.workspace;

import com.igor.tenantcrm.common.exceptions.ConflictException;
import com.igor.tenantcrm.workspace.dto.WorkspaceCreateRequest;
import com.igor.tenantcrm.workspace.dto.WorkspaceResponse;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class WorkspaceService {

    private final WorkspaceRepository repo;

    public WorkspaceService(WorkspaceRepository repo) {
        this.repo = repo;
    }

    public List<WorkspaceResponse> list(UUID tenantId) {
        return repo.findAllByTenantIdOrderByCreatedAtDesc(tenantId)
                .stream()
                .map(WorkspaceResponse::from)
                .toList();
    }

    public WorkspaceResponse create(UUID tenantId, WorkspaceCreateRequest req) {
        String name = req.name().trim();

        // checagem amigável antes do banco
        if (repo.existsByTenantIdAndNameIgnoreCase(tenantId, name)) {
            throw new ConflictException("workspace already exists");
        }

        try {
            Workspace w = new Workspace();
            w.setTenantId(tenantId);
            w.setName(name);

            Workspace saved = repo.save(w);
            return WorkspaceResponse.from(saved);

        } catch (DataIntegrityViolationException ex) {
            // fallback se estourar unique index
            throw new ConflictException("workspace already exists");
        }
    }
}





