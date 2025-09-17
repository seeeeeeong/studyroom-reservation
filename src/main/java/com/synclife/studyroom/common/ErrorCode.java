package com.synclife.studyroom.common;

public enum ErrorCode {

    // Room
    ROOM_NOT_FOUND("ROOM_NOT_FOUND", "회의실을 찾을 수 없습니다."),
    ROOM_NAME_REQUIRED("ROOM_NAME_REQUIRED", "회의실 이름은 필수입니다."),
    ROOM_LOCATION_REQUIRED("ROOM_LOCATION_REQUIRED", "회의실 위치는 필수입니다."),
    ROOM_CAPACITY_REQUIRED("ROOM_CAPACITY_REQUIRED", "수용인원은 필수입니다."),
    ROOM_CAPACITY_INVALID("ROOM_CAPACITY_INVALID", "수용인원은 1명 이상이어야 합니다."),

    // Reservation
    RESERVATION_NOT_FOUND("RESERVATION_NOT_FOUND", "예약을 찾을 수 없습니다."),
    RESERVATION_TIME_INVALID("RESERVATION_TIME_INVALID", "시작 시간은 종료 시간보다 이전이어야 합니다."),
    RESERVATION_TIME_CONFLICT("RESERVATION_TIME_CONFLICT", "이미 예약된 시간대입니다."),
    RESERVATION_CANCEL_FORBIDDEN("RESERVATION_CANCEL_FORBIDDEN", "예약을 취소할 권한이 없습니다."),

    // Authentication
    AUTH_TOKEN_REQUIRED("AUTH_TOKEN_REQUIRED", "인증 토큰이 필요합니다."),
    AUTH_TOKEN_INVALID("AUTH_TOKEN_INVALID", "유효하지 않은 인증 토큰입니다."),
    AUTH_FORBIDDEN("AUTH_FORBIDDEN", "접근 권한이 없습니다.");

    private final String code;
    private final String message;

    ErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
