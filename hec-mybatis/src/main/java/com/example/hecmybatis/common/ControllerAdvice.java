package com.example.hecmybatis.common;

import com.example.heccore.common.exception.ApiResponse;
import com.example.heccore.common.exception.ErrorCode;
import com.example.heccore.common.exception.HecCustomException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ControllerAdvice {
    private final static String SUFFIX = ": {}";

    @ExceptionHandler({HecCustomException.class})
    protected ResponseEntity<ApiResponse> handleHecCustomException(HecCustomException e) {
        log.info(e.getMessage());
        errorLog("HEC Custom Exception occurred", e);
        return ResponseEntity.status(e.getErrorCode().getStatus())
                .body(ApiResponse.failure(e.getErrorCode()));
    }

    @ExceptionHandler({HttpRequestMethodNotSupportedException.class})
    protected ResponseEntity<ApiResponse> handleHttpRequestMethodNotSupportedException(
            HttpRequestMethodNotSupportedException e) {
        errorLog("HttpRequestMethodNotSupportedException", e);
        return ResponseEntity.status(e.getStatusCode().value())
                .body(ApiResponse.failure(ErrorCode.BAD_REQUEST));
    }

    @ExceptionHandler({IllegalStateException.class})
    @ResponseStatus(HttpStatus.FORBIDDEN)
    protected ResponseEntity<ApiResponse> handleIllegalStateException(IllegalStateException e) {
        errorLog("IllegalStateException", e);
        return ResponseEntity.status(HttpStatus.FORBIDDEN.value())
                .body(ApiResponse.failure(ErrorCode.INTERNAL_SERVER_ERROR));
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    protected ResponseEntity<ApiResponse> handleValidationException(
            MethodArgumentNotValidException e) {
        errorLog("Method Argument Validation failed", e);
        BindingResult result = e.getBindingResult();
        StringBuilder errors = new StringBuilder();
        for (int i = 0; i < result.getFieldErrors().size(); i++) {
            FieldError error = result.getFieldErrors().get(i);
            errors.append(error.getField()).append(" : ");
            errors.append(error.getDefaultMessage());
            if (i < result.getFieldErrors().size() - 1) {
                errors.append(", ");
            } else {
                errors.append(".");
            }
        }
        return ResponseEntity.status(e.getStatusCode().value())
                .body(ApiResponse.failure(errors.toString(), ErrorCode.BAD_REQUEST));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse> handleHttpMessageNotReadableException(
            HttpMessageNotReadableException e) {
        errorLog("handleHttpMessageNotReadableException ", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .body(ApiResponse.failure(ErrorCode.INTERNAL_SERVER_ERROR));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse> handleException(Exception e) {
        errorLog("handleException", e);
        log.info("skdi");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .body(ApiResponse.failure(ErrorCode.INTERNAL_SERVER_ERROR));
    }

    private void errorLog(String errorMessage, Throwable throwable) {
        if (throwable instanceof HecCustomException) {
            ErrorCode errorCode = ((HecCustomException) throwable).getErrorCode();
            switch (errorCode.getLogType()) {
                case WARN -> log.warn(errorMessage + SUFFIX, errorCode.getMessage());
                case ERROR -> log.error(errorMessage + SUFFIX, errorCode.getMessage());
            }
        } else {
            log.error(errorMessage, throwable);
        }
    }


}
