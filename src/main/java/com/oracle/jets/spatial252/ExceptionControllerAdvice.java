package com.oracle.jets.spatial252;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ExceptionControllerAdvice extends ResponseEntityExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<Object> handleSpatial252Exception(
            Spatial252Exception ex, WebRequest request) {
        // TODO logging
        return handleExceptionInternal(ex, null, null, ex.getStatus(), request);
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(
            Exception ex, Object body, HttpHeaders headers, HttpStatus status,
            WebRequest request) {
        ErrorResponse error = createErrorResponse(ex, status);
        return super.handleExceptionInternal(ex, error, headers, status, request);
    }

    private ErrorResponse createErrorResponse(Exception ex, HttpStatus status) {
        // ex, statusがnullになることはない
        ErrorResponse error = new ErrorResponse(
                status.value(), status.getReasonPhrase(), ex.getMessage());
        return error;
    }

}
