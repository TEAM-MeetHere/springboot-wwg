package com.example.wherewego.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class JwtException {
    private final int statusCode;
    private final String message;
}
