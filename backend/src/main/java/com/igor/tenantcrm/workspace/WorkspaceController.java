package com.igor.tenantcrm.workspace;

import com.igor.tenantcrm.workspace.dto.WorkspaceCreateRequest;
import com.igor.tenantcrm.workspace.dto.WorkspaceResponse;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/workspaces")
public class WorkspaceController {

    private final WorkspaceService service;

    public WorkspaceController(WorkspaceService service) {
        this.service = service;
    }

    private UUID tenantIdFromToken() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getDetails() == null) {
            throw new IllegalStateException("Missing authentication details");
        }
        @SuppressWarnings("unchecked")
        Map<String, Object> details = (Map<String, Object>) auth.getDetails();
        String tenantId = (String) details.get("tenantId");
        return UUID.fromString(tenantId);
    }

    @GetMapping
    public List<WorkspaceResponse> list() {
        return service.list(tenantIdFromToken());
    }

    @PostMapping
    public WorkspaceResponse create(@Valid @RequestBody WorkspaceCreateRequest req) {
        return service.create(tenantIdFromToken(), req);
    }
}








