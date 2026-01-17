package com.igor.tenantcrm.common.api;

import java.time.OffsetDateTime;
import java.util.List;

public record ApiErrorResponse(
        String code,
        String message,
        String path,
        int status,
        OffsetDateTime timestamp,
        List<ApiErrorDetail> details
) {}


