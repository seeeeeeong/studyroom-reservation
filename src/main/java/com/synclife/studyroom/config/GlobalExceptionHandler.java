package com.synclife.studyroom.config;

import com.synclife.studyroom.common.StudyroomException;
import java.util.HashMap;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(StudyroomException.class)
    public ResponseEntity<ErrorResponse> handleStudyroomException(StudyroomException e) {
        ErrorResponse errorResponse = new ErrorResponse(
            e.getErrorCode().getCode(),
            e.getErrorCode().getMessage()
        );

        HttpStatus status = switch (e.getErrorCode()) {
            case ROOM_NOT_FOUND, RESERVATION_NOT_FOUND -> HttpStatus.NOT_FOUND;
            case UNAUTHORIZED -> HttpStatus.FORBIDDEN;
            case RESERVATION_TIME_CONFLICT -> HttpStatus.CONFLICT;
            default -> HttpStatus.BAD_REQUEST;
        };

        return ResponseEntity.status(status).body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorResponse> handleValidationException(MethodArgumentNotValidException e) {
        Map<String, String> errors = new HashMap<>();

        e.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        ValidationErrorResponse errorResponse = new ValidationErrorResponse("VALIDATION_FAILED", "Validation failed", errors);
        return ResponseEntity.badRequest().body(errorResponse);
    }

    public record ErrorResponse(String code, String message) {}

    public record ValidationErrorResponse(String code, String message, Map<String, String> fieldErrors) {}
}
