package com.igor.tenantcrm.common.api;

import java.time.OffsetDateTime;
import java.util.List;

public record ApiErrorResponse(
        OffsetDateTime timestamp,
        int status,
        String error,
        String message,
        String code,
        String path,
        String traceId,
        List<ApiErrorDetail> details
) {}

