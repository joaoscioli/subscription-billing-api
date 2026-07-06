package com.joaoscioli.billing.common;

import com.joaoscioli.billing.customers.CustomerNotFoundException;
import com.joaoscioli.billing.customers.DuplicateCustomerEmailException;
import com.joaoscioli.billing.organizations.DuplicateOrganizationSlugException;
import com.joaoscioli.billing.organizations.OrganizationNotFoundException;
import com.joaoscioli.billing.plans.DuplicatePlanCodeException;
import com.joaoscioli.billing.plans.PlanNotFoundException;
import com.joaoscioli.billing.subscriptions.DuplicateActiveSubscriptionException;
import com.joaoscioli.billing.subscriptions.SubscriptionNotFoundException;
import com.joaoscioli.billing.subscriptions.SubscriptionStateException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.UUID;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DuplicateOrganizationSlugException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    ApiErrorResponse handleDuplicateOrganizationSlug(
            DuplicateOrganizationSlugException exception,
            HttpServletRequest request
    ) {
        return error(HttpStatus.CONFLICT, exception.getMessage(), request);
    }

    @ExceptionHandler(OrganizationNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    ApiErrorResponse handleOrganizationNotFound(
            OrganizationNotFoundException exception,
            HttpServletRequest request
    ) {
        return error(HttpStatus.NOT_FOUND, exception.getMessage(), request);
    }

    @ExceptionHandler(DuplicateCustomerEmailException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    ApiErrorResponse handleDuplicateCustomerEmail(
            DuplicateCustomerEmailException exception,
            HttpServletRequest request
    ) {
        return error(HttpStatus.CONFLICT, exception.getMessage(), request);
    }

    @ExceptionHandler(CustomerNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    ApiErrorResponse handleCustomerNotFound(
            CustomerNotFoundException exception,
            HttpServletRequest request
    ) {
        return error(HttpStatus.NOT_FOUND, exception.getMessage(), request);
    }

    @ExceptionHandler(DuplicatePlanCodeException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    ApiErrorResponse handleDuplicatePlanCode(
            DuplicatePlanCodeException exception,
            HttpServletRequest request
    ) {
        return error(HttpStatus.CONFLICT, exception.getMessage(), request);
    }

    @ExceptionHandler(PlanNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    ApiErrorResponse handlePlanNotFound(
            PlanNotFoundException exception,
            HttpServletRequest request
    ) {
        return error(HttpStatus.NOT_FOUND, exception.getMessage(), request);
    }

    @ExceptionHandler(DuplicateActiveSubscriptionException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    ApiErrorResponse handleDuplicateActiveSubscription(
            DuplicateActiveSubscriptionException exception,
            HttpServletRequest request
    ) {
        return error(HttpStatus.CONFLICT, exception.getMessage(), request);
    }

    @ExceptionHandler(SubscriptionNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    ApiErrorResponse handleSubscriptionNotFound(
            SubscriptionNotFoundException exception,
            HttpServletRequest request
    ) {
        return error(HttpStatus.NOT_FOUND, exception.getMessage(), request);
    }

    @ExceptionHandler(SubscriptionStateException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    ApiErrorResponse handleSubscriptionState(
            SubscriptionStateException exception,
            HttpServletRequest request
    ) {
        return error(HttpStatus.CONFLICT, exception.getMessage(), request);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ApiErrorResponse handleValidation(
            MethodArgumentNotValidException exception,
            HttpServletRequest request
    ) {
        var details = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fieldError -> new ApiFieldError(fieldError.getField(), fieldError.getDefaultMessage()))
                .toList();

        return error(HttpStatus.BAD_REQUEST, "Request validation failed", request, details);
    }

    private ApiErrorResponse error(HttpStatus status, String message, HttpServletRequest request) {
        return error(status, message, request, List.of());
    }

    private ApiErrorResponse error(
            HttpStatus status,
            String message,
            HttpServletRequest request,
            List<ApiFieldError> details
    ) {
        return new ApiErrorResponse(
                OffsetDateTime.now(ZoneOffset.UTC),
                status.value(),
                status.getReasonPhrase(),
                message,
                request.getRequestURI(),
                correlationId(request),
                details
        );
    }

    private String correlationId(HttpServletRequest request) {
        var headerValue = request.getHeader("X-Correlation-Id");

        if (headerValue == null || headerValue.isBlank()) {
            return UUID.randomUUID().toString();
        }

        return headerValue;
    }
}
