package com.example.wherewego.exception;

public class JwtRequestException extends IllegalArgumentException{
    public JwtRequestException(String message){
        super(message);
    }
    public JwtRequestException(String message, Throwable cause){
        super(message, cause);
    }
}
