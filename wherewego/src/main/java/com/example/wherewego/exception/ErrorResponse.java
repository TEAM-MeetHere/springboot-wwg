package com.example.wherewego.exception;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class ErrorResponse {

    private int statusCode;
    private String message;
}
