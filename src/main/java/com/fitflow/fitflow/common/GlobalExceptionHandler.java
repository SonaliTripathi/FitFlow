package com.fitflow.fitflow.common;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.NoSuchElementException;

// KEEP: @RestControllerAdvice = one global interceptor for exceptions thrown by
// ANY controller. Without it, every exception becomes an ugly 500 with a stack
// trace. This is the "centralized exception handling" pattern — same idea as
// the module on my Brillio resume bullet.
@RestControllerAdvice
public class GlobalExceptionHandler {

    // Service throws NoSuchElementException -> client gets 404 + clean JSON
    @ExceptionHandler(NoSuchElementException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> notFound(NoSuchElementException e) {
        return Map.of("error", e.getMessage());
    }

    // Duplicate email -> 409 Conflict ("request conflicts with current state")
    @ExceptionHandler(IllegalStateException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Map<String, String> conflict(IllegalStateException e) {
        return Map.of("error", e.getMessage());
    }

    // @Valid failures land here -> 400 Bad Request with the first field message
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> validation(MethodArgumentNotValidException e) {
        return Map.of("error", e.getBindingResult().getFieldErrors().get(0).getDefaultMessage());
    }
}