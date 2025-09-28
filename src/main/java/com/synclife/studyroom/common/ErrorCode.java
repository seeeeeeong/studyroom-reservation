package com.synclife.studyroom.common;

public enum ErrorCode {
    ROOM_NOT_FOUND("ROOM_NOT_FOUND", "Room not found"),
    RESERVATION_NOT_FOUND("RESERVATION_NOT_FOUND", "Reservation not found"),
    RESERVATION_TIME_CONFLICT("RESERVATION_TIME_CONFLICT", "Reservation time conflicts with existing reservation"),
    RESERVATION_TIME_INVALID("RESERVATION_TIME_INVALID", "Invalid reservation time"),
    RESERVATION_TIME_NOT_ALIGNED("RESERVATION_TIME_NOT_ALIGNED", "Reservation time must be aligned to 30-minute slots"),
    RESERVATION_TIME_TOO_SHORT("RESERVATION_TIME_TOO_SHORT", "Reservation must be at least 30 minutes"),
    RESERVATION_OUTSIDE_OPERATING_HOURS("RESERVATION_OUTSIDE_OPERATING_HOURS", "Reservation must be within operating hours (9:00-22:00)"),
    UNAUTHORIZED("UNAUTHORIZED", "Unauthorized access");

    private final String code;
    private final String message;

    ErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() { return code; }
    public String getMessage() { return message; }
}
