package com.synclife.studyroom.dto.response;

import com.synclife.studyroom.domain.Reservation;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record ReservationResponse(
    Long id,
    Long roomId,
    String roomName,
    String userId,
    LocalDateTime startAt,
    LocalDateTime endAt
) {
    public static ReservationResponse from(Reservation reservation, String roomName) {
        return new ReservationResponse(
            reservation.getId(),
            reservation.getRoomId(),
            roomName,
            reservation.getUserId(),
            reservation.getStartAt(),
            reservation.getEndAt()
        );
    }
}
