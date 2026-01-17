package com.igor.tenantcrm.workspace.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record WorkspaceCreateRequest(
        @NotBlank(message = "name is required")
        @Size(min = 2, max = 80, message = "name must be between 2 and 80 characters")
        String name
) {}
