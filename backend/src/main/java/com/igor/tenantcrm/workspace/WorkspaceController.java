package com.igor.tenantcrm.workspace;

import com.igor.tenantcrm.workspace.dto.WorkspaceCreateRequest;
import com.igor.tenantcrm.workspace.dto.WorkspaceResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
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
    public ResponseEntity<WorkspaceResponse> create(@RequestBody WorkspaceCreateRequest req) {
        WorkspaceResponse created = service.create(tenantIdFromToken(), req);

        // se WorkspaceResponse tiver o id (recomendado), monte o Location:
        URI location = URI.create("/workspaces/" + created.id());

        return ResponseEntity.created(location).body(created);
    }
}







