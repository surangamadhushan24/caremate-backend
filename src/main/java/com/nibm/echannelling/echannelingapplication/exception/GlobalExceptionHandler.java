package com.nibm.echannelling.echannelingapplication.exception;

import com.nibm.echannelling.echannelingapplication.dto.ChatResponse;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

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

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ChatResponse> handleIllegalArgumentException(IllegalArgumentException ex) {
        ChatResponse response = new ChatResponse();
        response.setResponse(ex.getMessage());
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

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ChatResponse> handleGeneralException(Exception ex) {
        ChatResponse response = new ChatResponse();
        response.setResponse("An unexpected error occurred. Please try again later.");
        response.setSuggestedSpecialty(null);
        response.setRecommendedDoctorIds(new Long[]{});
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}