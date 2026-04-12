package com.example.UberApp.exception;

import com.example.UberApp.DTOs.ApiResponse;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 🔴 USER NOT FOUND
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleUser(UserNotFoundException ex) {
        return new ResponseEntity<>(
                ApiResponse.error(ex.getMessage(), "USER_NOT_FOUND"),
                HttpStatus.NOT_FOUND
        );
    }

    // 🔴 RIDE NOT FOUND
    @ExceptionHandler(RideNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleRide(RideNotFoundException ex) {
        return new ResponseEntity<>(
                ApiResponse.error(ex.getMessage(), "RIDE_NOT_FOUND"),
                HttpStatus.NOT_FOUND
        );
    }

    // 🔴 UNAUTHORIZED
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ApiResponse<Object>> handleAuth(UnauthorizedException ex) {
        return new ResponseEntity<>(
                ApiResponse.error(ex.getMessage(), "UNAUTHORIZED"),
                HttpStatus.UNAUTHORIZED
        );
    }

    // 🔴 DEFAULT
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleAll(Exception ex) {
        return new ResponseEntity<>(
                ApiResponse.error("SOMETHING WENT WRONG", "INTERNAL_ERROR"),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}