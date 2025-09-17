package com.synclife.studyroom.dto.response;

import com.synclife.studyroom.domain.Reservation;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record ReservationResponse(
    Long id,
    Long roomId,
    String roomName,
    String userId,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    LocalDateTime startAt,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    LocalDateTime endAt
) {
    public static ReservationResponse from(Reservation reservation) {
        return new ReservationResponse(
            reservation.getId(),
            reservation.getRoom().getId(),
            reservation.getRoom().getName(),
            reservation.getUserId(),
            reservation.getStartAt(),
            reservation.getEndAt()
        );
    }
}
