package com.example.wherewego.exception;

import com.example.wherewego.response.StatusCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(value = {ApiRequestException.class})
    public ResponseEntity<Object> handleApiRequestException(ApiRequestException exception) {
        ApiException apiException = new ApiException(
                StatusCode.BAD_REQUEST,
                exception.getMessage()
                // HTTP 400 -> Client Error
        );

        return new ResponseEntity<>(
                apiException,
                HttpStatus.OK
        );
    }
}
