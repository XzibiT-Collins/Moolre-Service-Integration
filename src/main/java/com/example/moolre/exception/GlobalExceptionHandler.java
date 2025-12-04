package com.example.moolre.exception;

import com.example.moolre.dto.CustomApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<CustomApiResponse> handleBadRequestException(BadRequestException ex){
        return ResponseEntity.badRequest().body(CustomApiResponse.error(ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<CustomApiResponse> handleException(Exception ex){
        return ResponseEntity.internalServerError().body(CustomApiResponse.error("Internal Server Error"));
    }
}
