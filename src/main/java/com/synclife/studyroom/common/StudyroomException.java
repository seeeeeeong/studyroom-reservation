package com.synclife.studyroom.common;

public class StudyroomException extends RuntimeException {

    private final ErrorCode errorCode;

    public StudyroomException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public StudyroomException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
