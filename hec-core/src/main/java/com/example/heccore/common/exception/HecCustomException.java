package com.example.heccore.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class HecCustomException extends RuntimeException {
    private final ErrorCode errorCode;
}
