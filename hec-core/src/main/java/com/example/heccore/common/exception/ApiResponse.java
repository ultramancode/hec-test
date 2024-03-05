package com.example.heccore.common.exception;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ApiResponse {

    private String message;
    private int status;

    public ApiResponse(String message, int status) {
        this.message = message;
        this.status = status;
    }
    public static ApiResponse failure(ErrorCode errorCode) {
        return new ApiResponse(errorCode.getMessage(), errorCode.getStatus());
    }

    public static ApiResponse failure(String message, ErrorCode errorCode) {
        return new ApiResponse(message, errorCode.getStatus());
    }
}
