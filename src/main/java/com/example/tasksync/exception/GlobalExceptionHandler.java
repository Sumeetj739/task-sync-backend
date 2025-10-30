package com.example.tasksync.exception;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import java.time.Instant;
import java.util.Map;

/**
 * 🌐 GlobalExceptionHandler
 *
 * This class provides centralized exception handling for all controllers
 * in the application. If any unhandled exception occurs in your code,
 * Spring will automatically send it here.
 *
 * Instead of returning a long stack trace or HTML error page,
 * this handler returns a clean, JSON-formatted error response.
 */
@ControllerAdvice  // Marks this class as a global exception handler across all controllers
public class GlobalExceptionHandler {

    /**
     * 🔹 handleAll()
     *
     * This method handles *any* exception that isn’t explicitly caught
     * somewhere else in the code.
     *
     * It builds a simple JSON response with:
     * - The time the error occurred (`timestamp`)
     * - The message from the exception (`message`)
     *
     * Example response:
     * {
     *   "timestamp": "2025-10-29T10:25:41.129Z",
     *   "message": "Task not found"
     * }
     *
     * @param ex  The exception object that was thrown
     * @param req The web request (contains details like URI, headers, etc.)
     * @return A JSON response with HTTP 500 (Internal Server Error)
     */
    @ExceptionHandler(Exception.class)  // Catch all exceptions of type java.lang.Exception
    public ResponseEntity<?> handleAll(Exception ex, WebRequest req) {

        // Build the JSON body for the response
        Map<String, Object> body = Map.of(
            "timestamp", Instant.now().toString(),  // When the error occurred
            "message", ex.getMessage()              // The exception message
        );

        // Return HTTP 500 (Internal Server Error) along with the JSON body
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(body);
    }
}

