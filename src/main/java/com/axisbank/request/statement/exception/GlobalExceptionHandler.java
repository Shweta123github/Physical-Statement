package com.axisbank.request.statement.exception;

import com.axisbank.request.statement.constants.StatementErrorMessages;
import com.axisbank.request.statement.response.PhysicalStatementResponse;
import com.axisbank.request.statement.util.ResponseUtils;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
@Log4j2
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus
    public ResponseEntity<PhysicalStatementResponse> handleValidationException(MethodArgumentNotValidException ex) {
        BindingResult result = ex.getBindingResult();
        List<FieldError> fieldErrors = result.getFieldErrors();

        List<String> errorMessages = new ArrayList<>();
        for (FieldError fieldError : fieldErrors) {
            errorMessages.add(fieldError.getDefaultMessage());
        }
        log.error("Request processing failed with validation errors: {}", errorMessages);
        return ResponseUtils.errorResponse(StatementErrorMessages.BAD_REQUEST, String.join(", ", errorMessages));
    }

    @ExceptionHandler(AxisException.class)
    @ResponseStatus
    public ResponseEntity<PhysicalStatementResponse> handleException(AxisException ex) {
        log.error("Unexpected error occurred: {}", ex.getMessage());
        return ResponseUtils.errorResponse(StatementErrorMessages.INTERNAL_SERVER_ERROR, ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus
    public ResponseEntity<PhysicalStatementResponse> handleGenericException(Exception ex) {
        log.error("Request processing failed with exception: {}", ex.getMessage());
        return ResponseUtils.errorResponse(StatementErrorMessages.INVALID_DATE_PATTERN,
               StatementErrorMessages.INVALID_DATE_PATTERN.getMessage());


    }
}
