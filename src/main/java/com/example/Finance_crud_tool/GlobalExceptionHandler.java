package com.example.Finance_crud_tool;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.Map;


@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public Map<String, Object> handleIllegalArgumentException(IllegalArgumentException ex, WebRequest request) {
        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("timestamp", System.currentTimeMillis());
        errorDetails.put("message", ex.getMessage());
        errorDetails.put("path", request.getDescription(false));
        return errorDetails;
    }

    @ExceptionHandler(Exception.class)
    public Map<String, Object> handleGeneralException(Exception ex, WebRequest request) {
        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("timestamp", System.currentTimeMillis());
        errorDetails.put("message", "Ocurrió un error interno, por favor contacte al soporte.");
        errorDetails.put("path", request.getDescription(false));
        return errorDetails;
    }


}
