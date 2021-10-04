package com.example.wherewego.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public class ApiException {
    private final int statusCode;
    private final String message;
}
