package com.example.wherewego.exception;

import com.example.wherewego.response.StatusCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class JwtExceptionHandler {
    @ExceptionHandler(value = {JwtRequestException.class})
    public ResponseEntity<Object> handleJwtRequestException(JwtRequestException exception) {
        JwtException jwtException = new JwtException(
                StatusCode.UNAUTHORIZED,
                exception.getMessage()
        );

        return new ResponseEntity<>(
                jwtException,
                HttpStatus.OK
        );
    }
}
