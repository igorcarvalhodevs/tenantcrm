package com.igor.tenantcrm.common.api;

public record ApiErrorDetail(
        String field,
        String message
) {}

