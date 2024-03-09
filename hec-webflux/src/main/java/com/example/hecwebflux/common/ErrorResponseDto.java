package com.example.hecwebflux.common;

import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public record ErrorResponseDto(
        String message,
        HttpStatus errorCode,
        LocalDateTime timestamp) {
}


