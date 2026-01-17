package com.igor.tenantcrm.common;

import com.igor.tenantcrm.common.api.ApiErrorDetail;
import com.igor.tenantcrm.common.api.ApiErrorResponse;
import com.igor.tenantcrm.common.exceptions.ApiException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest req) {
        List<ApiErrorDetail> details = ex.getBindingResult().getFieldErrors().stream()
                .map(this::toDetail)
                .toList();

        return build(HttpStatus.BAD_REQUEST, "Validation failed", "VALIDATION_ERROR", req, details);
    }

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ApiErrorResponse> handleApiException(ApiException ex, HttpServletRequest req) {
        // Aqui você pode mapear por tipo (BadRequestException -> 400, ConflictException -> 409 etc.)
        HttpStatus status = switch (ex.getCode()) {
            case "CONFLICT" -> HttpStatus.CONFLICT;
            case "NOT_FOUND" -> HttpStatus.NOT_FOUND;
            case "BAD_REQUEST" -> HttpStatus.BAD_REQUEST;
            default -> HttpStatus.BAD_REQUEST;
        };

        return build(status, ex.getMessage(), ex.getCode(), req, null);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiErrorResponse> handleAccessDenied(AccessDeniedException ex, HttpServletRequest req) {
        return build(HttpStatus.FORBIDDEN, "Access denied", "FORBIDDEN", req, null);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleUnexpected(Exception ex, HttpServletRequest req) {
        // Não exponha stack trace em API pública
        return build(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error", "INTERNAL_ERROR", req, null);
    }

    private ApiErrorDetail toDetail(FieldError fe) {
        return new ApiErrorDetail(fe.getField(), fe.getDefaultMessage());
    }

    private ResponseEntity<ApiErrorResponse> build(HttpStatus status, String message, String code,
                                                   HttpServletRequest req, List<ApiErrorDetail> details) {

        String traceId = UUID.randomUUID().toString();

        ApiErrorResponse body = new ApiErrorResponse(
                OffsetDateTime.now(),
                status.value(),
                status.getReasonPhrase(),
                message,
                code,
                req.getRequestURI(),
                traceId,
                details
        );

        return ResponseEntity.status(status).body(body);
    }
}


