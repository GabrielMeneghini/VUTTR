package com.apiRest.VUTTR.exceptions;

import com.auth0.jwt.exceptions.JWTCreationException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFound(ResourceNotFoundException e, HttpServletRequest request) {
        var status = HttpStatus.NOT_FOUND;
        var errorResponse = new ErrorResponse(status.value(), status.getReasonPhrase(), e.getMessage(), request.getRequestURI());

        return ResponseEntity.status(status).body(errorResponse);
    }

    @ExceptionHandler(NoUpdateDetectedException.class)
    public ResponseEntity<ErrorResponse> handleNoUpdateDetected(NoUpdateDetectedException e, HttpServletRequest request) {
        var status = HttpStatus.BAD_REQUEST;
        var errorResponse = new ErrorResponse(status.value(), status.getReasonPhrase(), e.getMessage(), request.getRequestURI());

        return ResponseEntity.status(status).body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValid(MethodArgumentNotValidException e, HttpServletRequest request) {
        var status = HttpStatus.BAD_REQUEST;
        var listMessages = e.getBindingResult().getFieldErrors().stream()
                .map(err -> err.getField() + ": " + err.getDefaultMessage()).toList();
        var errorResponse = new ErrorResponse(status.value(), status.getReasonPhrase(), listMessages, request.getRequestURI());

        return ResponseEntity.status(status).body(errorResponse);
    }

    @ExceptionHandler(HandlerMethodValidationException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(HandlerMethodValidationException e, HttpServletRequest request) {
        var status = HttpStatus.BAD_REQUEST;

        Map<String, String> fieldErrors = e.getValueResults().stream()
                .flatMap(vr -> vr.getResolvableErrors().stream())
                .collect(Collectors.toMap(
                        this::extractFieldName,
                        MessageSourceResolvable::getDefaultMessage,
                        (msg1, msg2) -> msg1
                ));

        var errorResponse = new ErrorResponse(
                LocalDateTime.now().toString(),
                status.value(),
                status.getReasonPhrase(),
                fieldErrors,
                request.getRequestURI()
        );

        return ResponseEntity.status(status).body(errorResponse);
    }

    @ExceptionHandler(JWTCreationException.class)
    public ResponseEntity<ErrorResponse> handleJWTCreation(JWTCreationException e, HttpServletRequest request) {
        var status = HttpStatus.INTERNAL_SERVER_ERROR;

        var errorResponse = new ErrorResponse(
                LocalDateTime.now().toString(),
                status.value(),
                status.getReasonPhrase(),
                "Failed to create JWT",
                request.getRequestURI()
        );

        return ResponseEntity.status(status).body(errorResponse);
    }

    private String extractFieldName(MessageSourceResolvable error) {
        if (error.getCodes() == null || error.getCodes().length == 0)
            return "unknown";

        String code = error.getCodes()[0];
        int lastDot = code.lastIndexOf('.');
        return lastDot != -1 ? code.substring(lastDot + 1) : code;
    }

    public record ErrorResponse(String timeStamp, int status, String error, Object message, String path) {
        public ErrorResponse(int status, String error, Object message, String path) {
            this(LocalDateTime.now().toString(), status, error, message, path);
        }
    }

}
