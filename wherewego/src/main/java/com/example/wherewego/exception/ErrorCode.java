package com.example.wherewego.exception;


import lombok.Getter;

public enum ErrorCode {

    NOT_NULL("ERROR_CODE_0001", "필수값이 누락되었습니다."),
    MIN_VALUE("ERROR_CODE_0002", "최소값보다 커야 합니다.");

    @Getter
    private String code;

    @Getter
    private String description;

    ErrorCode(String code, String description) {
        this.code = code;
        this.description = description;
    }
}
