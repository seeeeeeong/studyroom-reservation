package com.synclife.studyroom.domain;

import com.synclife.studyroom.common.ErrorCode;
import com.synclife.studyroom.common.StudyroomException;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "reservations")
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "room_id", nullable = false)
    private Long roomId;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "start_at", nullable = false)
    private LocalDateTime startAt;

    @Column(name = "end_at", nullable = false)
    private LocalDateTime endAt;

    protected Reservation() {}

    public Reservation(Long roomId, String userId, LocalDateTime startAt, LocalDateTime endAt) {
        this.roomId = roomId;
        this.userId = userId;
        this.startAt = startAt;
        this.endAt = endAt;
    }

    public static Reservation create(Long roomId, String userId, LocalDateTime startAt, LocalDateTime endAt) {
        return new Reservation(roomId, userId, startAt, endAt);
    }

    public boolean isOwnedBy(String userId) {
        return this.userId.equals(userId);
    }

    public Long getId() {
        return id;
    }

    public Long getRoomId() {
        return roomId;
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
}
