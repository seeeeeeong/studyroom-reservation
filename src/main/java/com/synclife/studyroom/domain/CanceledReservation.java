package com.synclife.studyroom.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "canceled_reservations")
public class CanceledReservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "original_reservation_id", nullable = false)
    private Long originalReservationId;

    @Column(name = "room_id", nullable = false)
    private Long roomId;

    @Column(name = "room_name", nullable = false)
    private String roomName;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "start_at", nullable = false)
    private LocalDateTime startAt;

    @Column(name = "end_at", nullable = false)
    private LocalDateTime endAt;

    @Column(name = "canceled_at", nullable = false)
    private LocalDateTime canceledAt;

    @Column(name = "canceled_by", nullable = false)
    private String canceledBy;

    protected CanceledReservation() {}

    public CanceledReservation(Long originalReservationId, Long roomId, String roomName,
        String userId, LocalDateTime startAt, LocalDateTime endAt,
        LocalDateTime canceledAt, String canceledBy) {
        this.originalReservationId = originalReservationId;
        this.roomId = roomId;
        this.roomName = roomName;
        this.userId = userId;
        this.startAt = startAt;
        this.endAt = endAt;
        this.canceledAt = canceledAt;
        this.canceledBy = canceledBy;
    }

    public static CanceledReservation fromReservation(Reservation reservation, String canceledBy) {
        return new CanceledReservation(
            reservation.getId(),
            reservation.getRoom().getId(),
            reservation.getRoom().getName(),
            reservation.getUserId(),
            reservation.getStartAt(),
            reservation.getEndAt(),
            LocalDateTime.now(),
            canceledBy
        );
    }

    public Long getId() {
        return id;
    }

    public Long getOriginalReservationId() {
        return originalReservationId;
    }

    public Long getRoomId() {
        return roomId;
    }

    public String getRoomName() {
        return roomName;
    }

    public String getUserId() {
        return userId;
    }

    public LocalDateTime getStartAt() {
        return startAt;
    }

    public LocalDateTime getEndAt() {
        return endAt;
    }

    public LocalDateTime getCanceledAt() {
        return canceledAt;
    }

    public String getCanceledBy() {
        return canceledBy;
    }
}
