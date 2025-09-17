package com.synclife.studyroom.config;

import com.synclife.studyroom.common.StudyroomException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(StudyroomException.class)
    public ResponseEntity<Map<String, String>> handleStudyroomException(StudyroomException e) {
        HttpStatus status = determineHttpStatus(e);

        Map<String, String> errorResponse = Map.of(
            "error", e.getErrorCode().getCode(),
            "message", e.getErrorCode().getMessage()
        );

        return ResponseEntity.status(status).body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationException(MethodArgumentNotValidException e) {
        String message = e.getBindingResult().getAllErrors().get(0).getDefaultMessage();

        Map<String, String> errorResponse = Map.of(
            "error", "VALIDATION_FAILED",
            "message", message
        );

        return ResponseEntity.badRequest().body(errorResponse);
    }

    private HttpStatus determineHttpStatus(StudyroomException e) {
        String errorCode = e.getErrorCode().getCode();

        if (errorCode.contains("NOT_FOUND")) {
            return HttpStatus.NOT_FOUND;
        }
        if (errorCode.contains("FORBIDDEN") || errorCode.equals("AUTH_FORBIDDEN")) {
            return HttpStatus.FORBIDDEN;
        }
        if (errorCode.contains("AUTH_TOKEN")) {
            return HttpStatus.UNAUTHORIZED;
        }
        if (errorCode.contains("CONFLICT")) {
            return HttpStatus.CONFLICT;
        }

        return HttpStatus.BAD_REQUEST;
    }
}
