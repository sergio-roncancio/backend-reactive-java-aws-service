package com.pragma.handler;

import com.pragma.domain.exceptions.BankException;
import com.pragma.domain.exceptions.error.Error;
import com.pragma.handler.response.ErrorResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.handler.ResponseStatusExceptionHandler;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.pragma.domain.exceptions.error.impl.ErrorDefinition.BASIC_VALIDATIONS;
import static com.pragma.domain.exceptions.error.impl.ErrorDefinition.INTERNAL_SERVER_ERROR;

@Slf4j
@ControllerAdvice
public class HandleError extends ResponseStatusExceptionHandler {

    @ExceptionHandler(BankException.class)
    public Mono<ResponseEntity<ErrorResponse>> handleBankException(BankException ex, ServerWebExchange exchange) {
        return createErrorResponse(ex, ex.getError(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public Mono<ResponseEntity<ErrorResponse>> handleGenericException(Exception ex, ServerWebExchange exchange) {
        return createErrorResponse(ex, INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public Mono<ResponseEntity<ErrorResponse>> handleConstraintViolationException(ConstraintViolationException ex, ServerWebExchange exchange) {
        return createErrorResponse(ex, BASIC_VALIDATIONS, HttpStatus.BAD_REQUEST)
                .map(response -> {
                    var body = response.getBody();
                    if (body != null) {
                        body.setErrorDetails(createErrorsConstrainsViolation(ex.getConstraintViolations()));
                    }
                    return response;
                });
    }

    private Mono<ResponseEntity<ErrorResponse>> createErrorResponse(Exception exception, Error error,
                                                                    HttpStatus httpStatus){
        log.error(exception.getMessage(), exception);
        var responseError = createErrorResponse(error.getMessage(), error.getCode());
        return Mono.just(ResponseEntity.status(httpStatus)
                .body(responseError));
    }

    private List<String> createErrorsConstrainsViolation(Set<ConstraintViolation<?>> constraintViolations){
        var errorList = new ArrayList<String>();
        for (var violation: constraintViolations){
            errorList.add(String.join(": ", violation.getPropertyPath().toString(),
                    violation.getMessage()));
        }
        return errorList;
    }

    private ErrorResponse createErrorResponse(String message, String code){
        return createErrorResponse(message, code, new ArrayList<>());
    }

    private ErrorResponse createErrorResponse(String message, String code, List<String> errorDetails){
        return ErrorResponse.builder()
                .message(message)
                .code(code)
                .errorDetails(errorDetails)
                .timestamp(LocalDateTime.now())
                .build();
    }

}
