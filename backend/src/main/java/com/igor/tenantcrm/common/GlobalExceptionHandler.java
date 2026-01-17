package com.igor.tenantcrm.common;

import com.igor.tenantcrm.common.api.ApiErrorDetail;
import com.igor.tenantcrm.common.api.ApiErrorResponse;
import com.igor.tenantcrm.common.exceptions.ApiException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.OffsetDateTime;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ApiErrorResponse> handleApi(ApiException ex, HttpServletRequest req) {
        var body = new ApiErrorResponse(
                ex.getCode(),
                ex.getMessage(),
                req.getRequestURI(),
                ex.getStatus().value(),
                OffsetDateTime.now(),
                List.of()
        );
        return ResponseEntity.status(ex.getStatus()).body(body);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest req) {
        List<ApiErrorDetail> details = ex.getBindingResult().getFieldErrors().stream()
                .map(this::toDetail)
                .toList();

        var body = new ApiErrorResponse(
                "VALIDATION_ERROR",
                "Validation failed",
                req.getRequestURI(),
                HttpStatus.BAD_REQUEST.value(),
                OffsetDateTime.now(),
                details
        );
        return ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiErrorResponse> handleUnreadable(HttpMessageNotReadableException ex, HttpServletRequest req) {
        var body = new ApiErrorResponse(
                "INVALID_JSON",
                "Malformed JSON request",
                req.getRequestURI(),
                HttpStatus.BAD_REQUEST.value(),
                OffsetDateTime.now(),
                List.of()
        );
        return ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiErrorResponse> handleForbidden(AccessDeniedException ex, HttpServletRequest req) {
        var body = new ApiErrorResponse(
                "FORBIDDEN",
                "Access denied",
                req.getRequestURI(),
                HttpStatus.FORBIDDEN.value(),
                OffsetDateTime.now(),
                List.of()
        );
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(body);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiErrorResponse> handleUnauthorized(AuthenticationException ex, HttpServletRequest req) {
        var body = new ApiErrorResponse(
                "UNAUTHORIZED",
                "Unauthorized",
                req.getRequestURI(),
                HttpStatus.UNAUTHORIZED.value(),
                OffsetDateTime.now(),
                List.of()
        );
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(body);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleGeneric(Exception ex, HttpServletRequest req) {
        var body = new ApiErrorResponse(
                "INTERNAL_ERROR",
                "Unexpected error",
                req.getRequestURI(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                OffsetDateTime.now(),
                List.of()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }

    private ApiErrorDetail toDetail(FieldError fe) {
        return new ApiErrorDetail(fe.getField(), fe.getDefaultMessage());
    }
}



