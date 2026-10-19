package com.nibm.echannelling.echannelingapplication.exception;

import com.nibm.echannelling.echannelingapplication.dto.ApiResponse;
import com.nibm.echannelling.echannelingapplication.dto.ChatResponse;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiResponse<Void>> handleBadCredentials(
            BadCredentialsException ex) {

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(new ApiResponse<>(
                        false,
                        "Invalid email or password",
                        null
                ));
    }


    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<Void>> handleIllegalArgument(
            IllegalArgumentException ex) {

        return ResponseEntity
                .badRequest()
                .body(new ApiResponse<>(
                        false,
                        ex.getMessage(),
                        null
                ));
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ApiResponse<Void>> handleInvalidCredentials(
            InvalidCredentialsException ex) {

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ApiResponse<>(
                        false, ex.getMessage(), null));
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiResponse<Void>> handleBadRequest(
            BadRequestException ex) {

        return ResponseEntity.badRequest()
                .body(new ApiResponse<>(
                        false, ex.getMessage(), null));
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidation(
            MethodArgumentNotValidException ex) {

        String message = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .findFirst()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .orElse("Validation failed");

        return ResponseEntity
                .badRequest()
                .body(new ApiResponse<>(
                        false,
                        message,
                        null
                ));
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleUnexpected(
            Exception ex) {


        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse<>(
                        false,
                        "An unexpected error occurred",
                        null
                ));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ChatResponse> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        ChatResponse response = new ChatResponse();
        response.setResponse("Database error: " + ex.getMessage());
        response.setSuggestedSpecialty(null);
        response.setRecommendedDoctorIds(new Long[]{});
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(InvalidDataAccessApiUsageException.class)
    public ResponseEntity<ChatResponse> handleInvalidDataAccessException(InvalidDataAccessApiUsageException ex) {
        ChatResponse response = new ChatResponse();
        response.setResponse("Invalid data access: " + ex.getMessage());
        response.setSuggestedSpecialty(null);
        response.setRecommendedDoctorIds(new Long[]{});
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }



    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ChatResponse> handleRuntimeException(RuntimeException ex) {
        ChatResponse response = new ChatResponse();
        response.setResponse(ex.getMessage());
        response.setSuggestedSpecialty(null);
        response.setRecommendedDoctorIds(new Long[]{});
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }


}