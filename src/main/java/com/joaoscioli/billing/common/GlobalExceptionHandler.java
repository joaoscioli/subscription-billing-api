package com.joaoscioli.billing.common;

import com.joaoscioli.billing.customers.CustomerNotFoundException;
import com.joaoscioli.billing.customers.DuplicateCustomerEmailException;
import com.joaoscioli.billing.organizations.DuplicateOrganizationSlugException;
import com.joaoscioli.billing.organizations.OrganizationNotFoundException;
import com.joaoscioli.billing.plans.DuplicatePlanCodeException;
import com.joaoscioli.billing.plans.PlanNotFoundException;
import com.joaoscioli.billing.subscriptions.DuplicateActiveSubscriptionException;
import com.joaoscioli.billing.subscriptions.SubscriptionNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
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

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ApiErrorResponse handleValidation(
            MethodArgumentNotValidException exception,
            HttpServletRequest request
    ) {
        var message = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .findFirst()
                .map(fieldError -> fieldError.getField() + " " + fieldError.getDefaultMessage())
                .orElse("Invalid request");

        return error(HttpStatus.BAD_REQUEST, message, request);
    }

    private ApiErrorResponse error(HttpStatus status, String message, HttpServletRequest request) {
        return new ApiErrorResponse(
                OffsetDateTime.now(ZoneOffset.UTC),
                status.value(),
                status.getReasonPhrase(),
                message,
                request.getRequestURI(),
                correlationId(request)
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
