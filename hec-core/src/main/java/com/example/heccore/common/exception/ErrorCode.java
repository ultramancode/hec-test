package com.example.heccore.common.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {
    /**
     * 400 BAD_REQUEST
     * 404 NOT_FOUND
     * 409 CONFLICT
     */

    BAD_REQUEST("요청에 오류가 있습니다.", LogType.ERROR, 400),
    NOT_FOUND("해당 요청을 찾을 수 없습니다.", LogType.ERROR, 404),
    USER_IS_NOT_EXIST("사용자가 존재 하지 않습니다.", LogType.ERROR, 404),
    ACCOUNT_IS_NOT_EXIST("계좌가 존재 하지 않습니다.", LogType.ERROR, 404),
    USER_IS_ALREADY_DELETED("이미 삭제된 사용자입니다.", LogType.ERROR, 409),
    ACCOUNT_IS_ALREADY_DELETED("이미 삭제된 계좌입니다.", LogType.ERROR, 409),
    ACCOUNT_NUMBER_IS_ALREADY_EXIST("이미 존재하는 계좌번호입니다.", LogType.ERROR, 409),
    DEPOSIT_IS_NOT_VALID("입금액은 0보다 커야 합니다.",LogType.ERROR,400),
    WITHDRAW_IS_NOT_VALID("출금액은 0보다 커야 합니다.",LogType.ERROR,400),
    BALANCE_IS_NOT_VALID("잔액이 부족합니다.",LogType.ERROR,400),
    INTERNAL_SERVER_ERROR("서버에 오류가 발생했습니다. 잠시 후 다시 시도해주세요.", LogType.ERROR, 500);

    private final String message;
    private final LogType logType;
    private final int status;

    ErrorCode(String message, LogType logType, int status) {
        this.message = message;
        this.logType = logType;
        this.status = status;
    }

    public enum LogType {
        WARN,
        ERROR
    }
}
